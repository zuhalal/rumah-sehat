import 'dart:convert';

PembayaranResponse pembayaranResponseFromJson(String str) =>
    PembayaranResponse.fromJson(json.decode(str));

String pembayaranResponseToJson(PembayaranResponse data) =>
    json.encode(data.toJson());

class PembayaranResponse {
  PembayaranResponse({
    required this.statusCode,
    required this.data,
    required this.message,
    required this.status,
  });

  int statusCode;
  TagihanResponse data;
  String message;
  String status;

  factory PembayaranResponse.fromJson(Map<String, dynamic> json) =>
      PembayaranResponse(
        statusCode: json["status_code"],
        data: TagihanResponse.fromJson(json["data"]),
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

class TagihanResponse {
  TagihanResponse({
    required this.kode,
    required this.isPaid,
  });

  String kode;
  bool isPaid;

  factory TagihanResponse.fromJson(Map<String, dynamic> json) =>
      TagihanResponse(
        kode: json["kode"],
        isPaid: json["isPaid"],
      );

  Map<String, dynamic> toJson() => {
        "kode": kode,
        "isPaid": isPaid,
      };
}
