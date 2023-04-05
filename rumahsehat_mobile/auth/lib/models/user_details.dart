// To parse this JSON data, do
//
//     final userDetails = userDetailsFromJson(jsonString);

import 'dart:convert';

UserDetails userDetailsFromJson(String str) => UserDetails.fromJson(json.decode(str));

String userDetailsToJson(UserDetails data) => json.encode(data.toJson());

class UserDetails {
  UserDetails({
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

  factory UserDetails.fromJson(Map<String, dynamic> json) => UserDetails(
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
