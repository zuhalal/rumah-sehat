// To parse this JSON data, do
//
//     final resepDetailResponse = resepDetailResponseFromJson(jsonString);

import 'dart:convert';
import 'package:resep/models/resep.dart';

ResepDetailResponse resepDetailResponseFromJson(String str) => ResepDetailResponse.fromJson(json.decode(str));

String resepDetailResponseToJson(ResepDetailResponse data) => json.encode(data.toJson());

class ResepDetailResponse {
  ResepDetailResponse({
    required this.statusCode,
    required this.data,
    required this.message,
    required this.status,
  });

  int statusCode;
  Resep? data;
  String message;
  String status;

  factory ResepDetailResponse.fromJson(Map<String, dynamic> json) => ResepDetailResponse(
    statusCode: json["status_code"],
    data: json["data"] == null ? null : Resep.fromJson(json["data"]),
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