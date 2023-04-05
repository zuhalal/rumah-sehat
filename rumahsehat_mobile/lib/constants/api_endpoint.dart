class ApiEndpoint {
  // change with IP network url (dev) and kirti url and port (prod)
  static const String baseUrl = "https://apap-069.cs.ui.ac.id/api/v1";
  // 172.29.112.1

  // auth
  static const String login = "$baseUrl/auth/login";
  static const String userDetails = "$baseUrl/auth/details";
  static const String regis = "$baseUrl/auth/regis";

  // pasien
  static const String topUpSaldo = "$baseUrl/pasien/topup";

  // tagihan
  static const String pembayaran = "$baseUrl/tagihan/pembayaran";
  static const String tagihan = "$baseUrl/tagihan";

  // appointment
  static const String addAppointment ="$baseUrl/appointment/add";
  static const String listDokter = "$baseUrl/dokter/list-dokter";
  static const String appointment = "$baseUrl/appointment";

  // resep
  static const String resep = "$baseUrl/resep";
}
