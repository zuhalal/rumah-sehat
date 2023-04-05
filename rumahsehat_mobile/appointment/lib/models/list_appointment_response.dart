// To parse this JSON data, do
//
//     final appointmentListResponse = appointmentListResponseFromJson(jsonString);

import 'dart:convert';

AppointmentListResponse appointmentListResponseFromJson(String str) => AppointmentListResponse.fromJson(json.decode(str));

String appointmentListResponseToJson(AppointmentListResponse data) => json.encode(data.toJson());

Appointment appointmentFromJson(String str) => Appointment.fromJson(json.decode(str));

String appointmentToJson(Appointment data) => json.encode(data.toJson());

class AppointmentListResponse {
    AppointmentListResponse({
        required this.statusCode,
        required this.data,
        required this.message,
        required this.status,
    });

    int statusCode;
    List<Appointment> data;
    String message;
    String status;

    factory AppointmentListResponse.fromJson(Map<String, dynamic> json) => AppointmentListResponse(
        statusCode: json["status_code"],
        data:  List<Appointment>.from(json["data"].map((x) => Appointment.fromJson(x))),
        message: json["message"],
        status: json["status"],
    );

    Map<String, dynamic> toJson() => {
        "status_code": statusCode,
        "data": data == null ? null : List<dynamic>.from(data!.map((x) => x.toJson())),
        "message": message,
        "status": status,
    };
}

class Appointment {
    Appointment({
        required this.kode,
        required this.waktuAwal,
        required this.isDone,
        required this.pasien,
        required this.dokter,
    });

    String kode;
    DateTime waktuAwal;
    bool isDone;
    Dokter pasien;
    Dokter dokter;

    factory Appointment.fromJson(Map<String, dynamic> json) => Appointment(
        kode: json["kode"],
        waktuAwal: DateTime.parse(json["waktuAwal"]),
        isDone: json["isDone"],
        pasien: Dokter.fromJson(json["pasien"]),
        dokter: Dokter.fromJson(json["dokter"]),
    );

    Map<String, dynamic> toJson() => {
        "kode": kode,
        "waktuAwal": waktuAwal.toIso8601String(),
        "isDone": isDone,
        "pasien": pasien.toJson(),
        "dokter": dokter.toJson(),
    };
}

class Dokter {
    Dokter({
        required this.uuid,
        required this.nama,
        required this.role,
        required this.username,
        required this.email,
        required this.isSso,
        required this.tarif,
        required this.saldo,
        required this.umur,
    });

    String uuid;
    String nama;
    String role;
    String username;
    String email;
    bool isSso;
    int? tarif;
    int? saldo;
    int? umur;

    factory Dokter.fromJson(Map<String, dynamic> json) => Dokter(
        uuid: json["uuid"],
        nama: json["nama"],
        role: json["role"],
        username: json["username"],
        email: json["email"],
        isSso: json["isSso"],
        tarif: json["tarif"] == null ? null : json["tarif"],
        saldo: json["saldo"] == null ? null : json["saldo"],
        umur: json["umur"] == null ? null : json["umur"],
    );

    Map<String, dynamic> toJson() => {
        "uuid": uuid,
        "nama": nama,
        "role": role,
        "username": username,
        "email": email,
        "isSso": isSso,
        "tarif": tarif == null ? null : tarif,
        "saldo": saldo == null ? null : saldo,
        "umur": umur == null ? null : umur,
    };
}
