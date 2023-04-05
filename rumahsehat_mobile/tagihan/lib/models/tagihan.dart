// To parse this JSON data, do
//
//     final tagihanResponse = tagihanResponseFromJson(jsonString);

import 'dart:convert';

TagihanResponse tagihanResponseFromJson(String str) => TagihanResponse.fromJson(json.decode(str));

String tagihanResponseToJson(TagihanResponse data) => json.encode(data.toJson());

Tagihan tagihanFromJson(String str) => Tagihan.fromJson(json.decode(str));

String tagihanToJson(Tagihan data) => json.encode(data.toJson());

class TagihanResponse {
    TagihanResponse({
        required this.statusCode,
        required this.data,
        required this.message,
        required this.status,
    });

    int statusCode;
    List<Tagihan> data;
    String message;
    String status;

    factory TagihanResponse.fromJson(Map<String, dynamic> json) => TagihanResponse(
        statusCode: json["status_code"],
        data: List<Tagihan>.from(json["data"].map((x) => Tagihan.fromJson(x))),
        message: json["message"],
        status: json["status"],
    );

    Map<String, dynamic> toJson() => {
        "status_code": statusCode,
        "data": List<dynamic>.from(data.map((x) => x.toJson())),
        "message": message,
        "status": status,
    };
}

class Tagihan {
    Tagihan({
        required this.kode,
        required this.tanggalTerbuat,
        required this.tanggalBayar,
        required this.isPaid,
        required this.jumlahTagihan,
        required this.kodeAppointment,
    });

    String kode;
    DateTime tanggalTerbuat;
    String? tanggalBayar;
    bool isPaid;
    int jumlahTagihan;
    KodeAppointment kodeAppointment;

    factory Tagihan.fromJson(Map<String, dynamic> json) => Tagihan(
        kode: json["kode"],
        tanggalTerbuat: DateTime.parse(json["tanggalTerbuat"]),
        tanggalBayar: json["tanggalBayar"] == null ? null : json["tanggalBayar"],
        isPaid: json["isPaid"],
        jumlahTagihan: json["jumlahTagihan"],
        kodeAppointment: KodeAppointment.fromJson(json["kodeAppointment"]),
    );

    Map<String, dynamic> toJson() => {
        "kode": kode,
        "tanggalTerbuat": tanggalTerbuat.toIso8601String(),
        "tanggalBayar": tanggalBayar,
        "isPaid": isPaid,
        "jumlahTagihan": jumlahTagihan,
        "kodeAppointment": kodeAppointment.toJson(),
    };
}

class KodeAppointment {
    KodeAppointment({
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

    factory KodeAppointment.fromJson(Map<String, dynamic> json) => KodeAppointment(
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
