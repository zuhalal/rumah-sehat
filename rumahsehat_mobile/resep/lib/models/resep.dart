import 'dart:convert';

Resep resepFromJson(String str) => Resep.fromJson(json.decode(str));

String resepToJson(Resep data) => json.encode(data.toJson());

class Resep {
  Resep({
    required this.id,
    required this.isDone,
    required this.createdAt,
    required this.confirmerUuid,
    required this.kodeAppointment,
    required this.listJumlah,
  });

  int id;
  bool isDone;
  String createdAt;
  ConfirmerUuid? confirmerUuid;
  KodeAppointment kodeAppointment;
  List<ListJumlah> listJumlah;

  factory Resep.fromJson(Map<String, dynamic> json) => Resep(
    id: json["id"],
    isDone: json["isDone"],
    createdAt: json["createdAt"],
    confirmerUuid: json["confirmerUUID"] == null ? null : ConfirmerUuid.fromJson(json["confirmerUUID"]),
    kodeAppointment: KodeAppointment.fromJson(json["kodeAppointment"]),
    listJumlah: List<ListJumlah>.from(json["listJumlah"].map((x) => ListJumlah.fromJson(x))),
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "isDone": isDone,
    "createdAt": createdAt,
    "confirmerUUID": confirmerUuid != null ? confirmerUuid?.toJson() : null,
    "kodeAppointment": kodeAppointment.toJson(),
    "listJumlah": List<dynamic>.from(listJumlah.map((x) => x.toJson())),
  };
}

class ConfirmerUuid {
  ConfirmerUuid({
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

  factory ConfirmerUuid.fromJson(Map<String, dynamic> json) => ConfirmerUuid(
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

class KodeAppointment {
  KodeAppointment({
    required this.kode,
    required this.waktuAwal,
    required this.isDone,
    required this.pasien,
    required this.dokter,
  });

  String kode;
  String waktuAwal;
  bool isDone;
  ConfirmerUuid pasien;
  ConfirmerUuid dokter;

  factory KodeAppointment.fromJson(Map<String, dynamic> json) => KodeAppointment(
    kode: json["kode"],
    waktuAwal: json["waktuAwal"],
    isDone: json["isDone"],
    pasien: ConfirmerUuid.fromJson(json["pasien"]),
    dokter: ConfirmerUuid.fromJson(json["dokter"]),
  );

  Map<String, dynamic> toJson() => {
    "kode": kode,
    "waktuAwal": waktuAwal,
    "isDone": isDone,
    "pasien": pasien.toJson(),
    "dokter": dokter.toJson(),
  };
}

class ListJumlah {
  ListJumlah({
    required this.id,
    required this.obat,
    required this.kuantitas,
  });

  int id;
  Obat obat;
  int kuantitas;

  factory ListJumlah.fromJson(Map<String, dynamic> json) => ListJumlah(
    id: json["id"],
    obat: Obat.fromJson(json["obat"]),
    kuantitas: json["kuantitas"],
  );

  Map<String, dynamic> toJson() => {
    "id": id,
    "obat": obat.toJson(),
    "kuantitas": kuantitas,
  };
}

class Obat {
  Obat({
    required this.idObat,
    required this.namaObat,
    required this.harga,
    required this.stok,
  });

  String idObat;
  String namaObat;
  int harga;
  int stok;

  factory Obat.fromJson(Map<String, dynamic> json) => Obat(
    idObat: json["idObat"],
    namaObat: json["namaObat"],
    harga: json["harga"],
    stok: json["stok"],
  );

  Map<String, dynamic> toJson() => {
    "idObat": idObat,
    "namaObat": namaObat,
    "harga": harga,
    "stok": stok,
  };
}
