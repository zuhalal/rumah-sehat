// To parse this JSON data, do
//
//     final userDetailResponse = userDetailResponseFromJson(jsonString);

import 'dart:convert';

UserDetailResponse userDetailResponseFromJson(String str) => UserDetailResponse.fromJson(json.decode(str));

String userDetailResponseToJson(UserDetailResponse data) => json.encode(data.toJson());

class UserDetailResponse {
  UserDetailResponse({
    required this.statusCode,
    required this.data,
    required this.message,
    required this.status,
  });

  dynamic statusCode;
  Data data;
  dynamic message;
  dynamic status;

  factory UserDetailResponse.fromJson(Map<String, dynamic> json) => UserDetailResponse(
    statusCode: json["status_code"],
    data: Data.fromJson(json["data"]),
    message: json["message"],
    status: json["status"],
  );

  Map<String, dynamic> toJson() => {
    "status_code": statusCode,
    "data": data.toJson(),
    "message": message,
    "status": status,
  };
}

class Data {
  Data({
    required this.uuid,
    required this.nama,
    required this.role,
    required this.username,
    required this.email,
    required this.isSso,
    required this.saldo,
    required this.umur,
  });

  String uuid;
  String nama;
  String role;
  String username;
  String email;
  bool isSso;
  int saldo;
  int umur;

  factory Data.fromJson(Map<String, dynamic> json) => Data(
    uuid: json["uuid"],
    nama: json["nama"],
    role: json["role"],
    username: json["username"],
    email: json["email"],
    isSso: json["isSso"],
    saldo: json["saldo"],
    umur: json["umur"],
  );

  Map<String, dynamic> toJson() => {
    "uuid": uuid,
    "nama": nama,
    "role": role,
    "username": username,
    "email": email,
    "isSso": isSso,
    "saldo": saldo,
    "umur": umur,
  };
}
