import 'dart:convert';

TopUpResponse topUpResponseFromJson(String str) => TopUpResponse.fromJson(json.decode(str));

String topUpResponseToJson(TopUpResponse data) => json.encode(data.toJson());

class TopUpResponse {
  TopUpResponse({
    required this.statusCode,
    required this.data,
    required this.message,
    required this.status,
  });

  int statusCode;
  SaldoResponse data;
  String message;
  String status;

  factory TopUpResponse.fromJson(Map<String, dynamic> json) => TopUpResponse(
    statusCode: json["status_code"],
    data: SaldoResponse.fromJson(json["data"]),
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

class SaldoResponse {
  SaldoResponse({
    required this.saldo,
    required this.username,
  });

  int saldo;
  String username;

  factory SaldoResponse.fromJson(Map<String, dynamic> json) => SaldoResponse(
    saldo: json["saldo"],
    username: json["username"],
  );

  Map<String, dynamic> toJson() => {
    "saldo": saldo,
    "username": username,
  };
}
