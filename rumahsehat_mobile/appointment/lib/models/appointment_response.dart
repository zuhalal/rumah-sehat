import 'dart:convert';

PostAppointmentResponse postAppointmentResponseFromJson(String str) => PostAppointmentResponse.fromJson(json.decode(str));

class PostAppointmentResponse {
  PostAppointmentResponse({
    required this.statusCode,
    required this.message,
    required this.status,
  });

  int statusCode;
  String message;
  String status;

  factory PostAppointmentResponse.fromJson(Map<String, dynamic> json) => PostAppointmentResponse(
    statusCode: json["status_code"],
    message: json["message"],
    status: json["status"],
  );
}
