import 'dart:convert';

Dokter dokterFromJson(String str) => Dokter.fromJson(json.decode(str));
String dokterToJson(Dokter data) => json.encode(data.toJson());

class Dokter {
  Dokter({
    required this.uuid,
    required this.nama,
    required this.tarif
  });

  String uuid;
  String nama;
  int? tarif;

  factory Dokter.fromJson(Map<String, dynamic> json) => Dokter(
      uuid: json["uuid"],
      nama: json["nama"],
      tarif: json["tarif"] == null ? null : json["tarif"]
  );

  Map<String, dynamic> toJson() => {
    "uuid": uuid,
    "nama": nama,
    "tarif": tarif == null ? null : tarif,
  };
}

