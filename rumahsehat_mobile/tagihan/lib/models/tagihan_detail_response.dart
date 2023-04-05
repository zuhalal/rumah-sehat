import 'dart:convert';
import 'package:tagihan/models/tagihan.dart';

TagihanDetailResponse tagihanDetailResponseFromJson(String str) =>
    TagihanDetailResponse.fromJson(json.decode(str));

String tagihanDetailResponseToJson(TagihanDetailResponse data) =>
    json.encode(data.toJson());

class TagihanDetailResponse {
  TagihanDetailResponse({
    required this.statusCode,
    required this.data,
    required this.message,
    required this.status,
  });

  int statusCode;
  Tagihan? data;
  String message;
  String status;

  factory TagihanDetailResponse.fromJson(Map<String, dynamic> json) =>
      TagihanDetailResponse(
        statusCode: json["status_code"],
        data: json["data"] == null ? null : Tagihan.fromJson(json["data"]),
        message: json["message"],
        status: json["status"],
      );

  Map<String, dynamic> toJson() => {
        "status_code": statusCode,
        "data": data?.toJson(),
        "message": message,
        "status": status,
      };
}
