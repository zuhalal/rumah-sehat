import 'dart:convert';

PostTagihanResponse postTagihanResponseFromJson(String str) => PostTagihanResponse.fromJson(json.decode(str));

class PostTagihanResponse {
  PostTagihanResponse({
    required this.statusCode,
    required this.message,
    required this.status,
  });

  int statusCode;
  String message;
  String status;

  factory PostTagihanResponse.fromJson(Map<String, dynamic> json) => PostTagihanResponse(
    statusCode: json["status_code"],
    message: json["message"],
    status: json["status"],
  );
}
