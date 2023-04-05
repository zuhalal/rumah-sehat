import 'dart:convert';

DetailAppointmentResponse detailAppointmentResponseFromJson(String str) => DetailAppointmentResponse.fromJson(json.decode(str));

String detailAppointmentResponseToJson(DetailAppointmentResponse data) => json.encode(data.toJson());

class DetailAppointmentResponse {
  DetailAppointmentResponse({
    required this.statusCode,
    required this.status,
    required this.kode,
    required this.waktuAwal,
    required this.statusAppointment,
    required this.dokter,
    required this.pasien,
    required this.resep
  });

  int statusCode;
  String status;
  String kode;
  DateTime waktuAwal;
  bool statusAppointment;
  String dokter;
  String pasien;
  int? resep;

  factory DetailAppointmentResponse.fromJson(Map<String, dynamic> json) => DetailAppointmentResponse(
    statusCode: json["status_code"],
    status: json["status"],
    kode: json["kode"],
    waktuAwal: DateTime.parse(json["waktuAwal"]),
    statusAppointment: json["statusAppointment"],
    dokter: json["dokter"],
    pasien: json["pasien"],
    resep: json["resep"]
  );

  Map<String, dynamic> toJson() => {
    "status_code": statusCode,
    "status": status,
  };
}
