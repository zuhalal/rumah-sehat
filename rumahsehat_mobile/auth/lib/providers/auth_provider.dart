import 'package:auth/models/user_details.dart';
import 'package:auth/models/user_details_response.dart';
import 'package:flutter/material.dart';
import 'dart:convert';

import 'package:auth/models/user_token.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'package:rumahsehat_mobile/constants/api_endpoint.dart';

class AuthProvider with ChangeNotifier {
  UserDetails? user;
  bool isLoggedIn = false;
  String message = "";
  String isValid = "";
  String? jwtToken;

  UserDetails? getUser() {
    return user;
  }

  void setUser(UserDetails? data) {
    user = data;
    notifyListeners();
  }

  String? getJwtToken() {
    return jwtToken;
  }

  Future<bool> setToken(String value) async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.setString('token', value);
  }

  Future<String?> getToken() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.getString('token');
  }

  Future<bool?> removeToken() async {
    final SharedPreferences prefs = await SharedPreferences.getInstance();
    return prefs.remove('token');
  }

  Future<UserToken?> fetchToken(String username, String password) async {
    try {
      // clear the old token (if any) and used to handle bug in development
      // removeToken();
      // isLoggedIn = false;
      // setUser(null);
      // jwtToken = null;
      // notifyListeners();

      var url = Uri.parse(ApiEndpoint.login);
      var response = await http.post(url,
          body: json.encode({"username": username, "password": password}),
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          });

      UserToken? token;

      if (response.statusCode == 200) {
        message = "";
        notifyListeners();

        token = userTokenFromJson(response.body);
        await setToken(token.token);
      } else if (response.statusCode == 401) {
        message = "Username atau password salah";
        notifyListeners();
        return null;
      }
      return token;
    } catch (e) {
      print(e.toString());
      message = "Terjadi kesalahan di sisi server. Silahkan coba lagi nanti";
      return null;
    }
  }

  Future<UserDetails?> fetchUserDetails() async {
    try {
      late UserDetails userDetails;
      var tokenTemp = await getToken();

      var url = Uri.parse(ApiEndpoint.userDetails);
      var response = await http.get(url, headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': 'Bearer $tokenTemp',
      });

      UserDetailResponse userDetailResponse =
          userDetailResponseFromJson(response.body);

      if (response.statusCode == 200 &&
          userDetailResponse.status == "success") {
        userDetails = userDetailsFromJson(json.encode(userDetailResponse.data));
        isLoggedIn = true;
        jwtToken = tokenTemp;
        notifyListeners();
      }

      setUser(userDetails);
      return userDetails;
    } catch (e) {
      print(e.toString());
      return null;
    }
  }

  Future<String?> fetchRegisData(String username, String password, String nama,
      String email, int umur) async {
    try {
      // clear the old token (if any) and used to handle bug in development
      removeToken();

      var url = Uri.parse(ApiEndpoint.regis);
      var response = await http.post(url,
          body: json.encode({
            "username": username,
            "password": password,
            "nama": nama,
            "email": email,
            "umur": umur
          }),
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          });

      Map<String, dynamic> data = jsonDecode(response.body);

      if (data["status_code"] == 200) {
        isValid = "success";
        notifyListeners();
      } else if (data["status_code"] == 400) {
        isValid = "used email or uname";
        notifyListeners();
      } else {
        isValid = "error";
      }
    } catch (e) {
      return e.toString();
    }
  }

  Future<void> logout() async {
    await removeToken();
    isLoggedIn = false;
    setUser(null);
    notifyListeners();
  }
}
