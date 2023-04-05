import 'dart:convert';

ProfileResponse profileResponseFromJson(String str) => ProfileResponse.fromJson(json.decode(str));

String profileResponseToJson(ProfileResponse data) => json.encode(data.toJson());

class ProfileResponse {
  ProfileResponse({
    required this.statusCode,
    required this.status,
    required this.username,
    required this.email,
    required this.nama,
    required this.saldo,
  });

  int statusCode;
  String status;
  String username;
  String email;
  String nama;
  int saldo;

  factory ProfileResponse.fromJson(Map<String, dynamic> json) => ProfileResponse(
    statusCode: json["status_code"],
    status: json["status"],
    username: json["username"],
    email: json["email"],
    nama: json["nama"],
    saldo: json["saldo"],
  );

  Map<String, dynamic> toJson() => {
    "status_code": statusCode,
    "status": status,
  };
}
