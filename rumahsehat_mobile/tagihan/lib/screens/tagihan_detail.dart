import 'dart:convert';
import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;
import 'package:auth/providers/auth_provider.dart';

import 'package:flutter/material.dart';

import 'package:tagihan/models/tagihan.dart';
import 'package:tagihan/models/tagihan_detail_response.dart';
import 'package:tagihan/models/pembayaran_response.dart';

import 'package:rumahsehat_mobile/constants/api_endpoint.dart';

import 'package:rumahsehat_mobile/widgets/drawer/main_drawer.dart';

class TagihanDetailScreen extends StatefulWidget {
  static const routeName = '/tagihan/detail';
  const TagihanDetailScreen({Key? key, required this.kode}): super(key: key);

  final String kode;

  @override
  State<TagihanDetailScreen> createState() => _TagihanDetailScreenState();
}

class _TagihanDetailScreenState extends State<TagihanDetailScreen> {
  @override
  Future<Tagihan?> fetchTagihanDetails(String? token) async {
    try {
      late Tagihan? tagihan;
      // late var args = ModalRoute.of(context)!.settings.arguments;
      //   String id = args["id"];
      var url = Uri.parse("${ApiEndpoint.baseUrl}/tagihan/${widget.kode}");

      var response = await http.get(url, headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': 'Bearer $token',
      });

      TagihanDetailResponse tagihanDetailResponse =
          tagihanDetailResponseFromJson(response.body);

      if (response.statusCode == 200 &&
          tagihanDetailResponse.status == "success") {
        tagihan = tagihanFromJson(json.encode(tagihanDetailResponse.data));
      } else {
        tagihan = null;
      }

      return tagihan;
    } catch (e) {
      print(e.toString());
      return null;
    }
  }

  TextEditingController kodeController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);
    Future<PembayaranResponse?> pembayaran(String? token) async {
      try {
        var url = Uri.parse(ApiEndpoint.pembayaran);

        var response = await http.post(url,
            body: json.encode({"kode": kodeController.text}),
            headers: {
              'Content-Type': 'application/json',
              'Accept': 'application/json',
              'Authorization': 'Bearer $token',
            });

        PembayaranResponse pembayaranResponse =
            pembayaranResponseFromJson(response.body);

        return pembayaranResponse;
      } catch (e) {
        print(e.toString());
        return null;
      }
    }

    showSuccessDialog(BuildContext context) {
      // set up the button
      Widget okButton = ElevatedButton(
        style: ElevatedButton.styleFrom(
          textStyle: const TextStyle(fontSize: 20),
          padding: const EdgeInsets.fromLTRB(12.0, 8.0, 12.0, 8.0),
          shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(8.0)),
        ),
        onPressed: () => Navigator.pop(context),
        child: const Text('OK'),
      );

      AlertDialog alert = AlertDialog(
        title: const Text("Pembayaran Berhasil!"),
        content: const Text("Tagihan telah dibayarkan"),
        actions: [
          okButton,
        ],
      ); // show the dialog
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return alert;
        },
      );
    }

    showFailedDialog(BuildContext context) {
      // set up the button
      Widget okButton = TextButton(
          onPressed: () => Navigator.pop(context), child: const Text('OK'));

      AlertDialog alert = AlertDialog(
        title: const Text("Pembayaran Gagal"),
        content:
            const Text("Pembayaran tagihan gagal, pastikan saldo anda cukup."),
        actions: [
          okButton,
        ],
      ); // show the dialog
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return alert;
        },
      );
    }

    showAlertConfirmationDialog(BuildContext context) {
      // set up the buttons
      Widget cancelButton = TextButton(
          onPressed: () => Navigator.pop(context), child: const Text('Batal'));

      Widget confirmationButton = ElevatedButton(
          style: ElevatedButton.styleFrom(
              textStyle: const TextStyle(fontSize: 20),
              padding: const EdgeInsets.fromLTRB(12.0, 8.0, 12.0, 8.0),
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8.0))),
          onPressed: () async {
            PembayaranResponse? response =
                await pembayaran(authProvider.jwtToken);

            if (response?.statusCode == 200 && response?.status == "success") {
              await authProvider.fetchUserDetails();
              Navigator.pop(context);
              showSuccessDialog(context);
            } else {
              showFailedDialog(context);
            }
          },
          child: const Text("Konfirmasi"));

      AlertDialog alert = AlertDialog(
        title: const Text("Pembayaran"),
        content: const Text("Silakan konfirmasi pembayaran"),
        actions: [cancelButton, confirmationButton],
      ); // show the dialog
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return alert;
        },
      );
    }

    return Scaffold(
        appBar: AppBar(
          title: const Text("Detail Tagihan"),
        ),
        drawer: const MainDrawer(),
        body: SingleChildScrollView(
          child: FutureBuilder<Tagihan?>(
            future: fetchTagihanDetails(authProvider
                .jwtToken), // a previously-obtained Future<String> or null
            builder: (BuildContext context, AsyncSnapshot<Tagihan?> snapshot) {
              if (snapshot.data == null) {
                if (snapshot.connectionState == ConnectionState.done) {
                  return Padding(
                    padding: EdgeInsets.all(12),
                    child: Center(
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        crossAxisAlignment: CrossAxisAlignment.center,
                        children: [
                          const Text(
                            "Detail Tagihan tidak ditemukan",
                            style: TextStyle(fontSize: 16),
                          ),
                          const SizedBox(height: 8),
                          ElevatedButton(
                              style: ElevatedButton.styleFrom(
                                backgroundColor: Colors.blue,
                                minimumSize: Size.fromHeight(40),
                              ),
                              onPressed: () {
                                Navigator.pushNamed(context, '/home');
                              },
                              child: const Text(
                                'Kembali ke Beranda',
                                style: TextStyle(
                                  fontSize: 16,
                                ),
                              )),
                        ],
                      ),
                    ),
                  );
                }
                return const Center(child: CircularProgressIndicator());
              }

              if (!snapshot.hasData) {
                return Column(
                  children: const [
                    Text(
                      "Tagihan detail tidak ditemukan",
                      style: TextStyle(fontSize: 16),
                    ),
                    SizedBox(height: 8),
                  ],
                );
              } else {
                final data = snapshot.data as Tagihan;
                final kode = data.kode.toString();
                final tanggalTerbuat =
                    data.tanggalTerbuat.toString();
                final isPaid = data.isPaid.toString();
                final tanggalBayar = isPaid == "true"
                    ? data.tanggalBayar.toString()
                    : "Belum Bayar";
                final jumlahTagihan = data.jumlahTagihan.toString();
                final kodeAppointment = data.kodeAppointment.kode.toString();
                kodeController.text = data.kode.toString();

                return Padding(
                  padding: const EdgeInsets.all(20),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Card(
                        elevation: 4,
                        shadowColor: Colors.black,
                        shape: RoundedRectangleBorder(
                          side: const BorderSide(width: 1, color: Colors.white),
                          borderRadius: BorderRadius.circular(8),
                        ),
                        child: Container(
                          padding: const EdgeInsets.all(12),
                          width: double.maxFinite,
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Center(
                                child: Text("Detail Tagihan",
                                    style: TextStyle(
                                        fontSize: 16,
                                        fontWeight: FontWeight.bold)),
                              ),
                              const SizedBox(height: 8),
                              Text('Kode: $kode'),
                              SizedBox(
                                  child: isPaid == "true"
                                      ? Text('TanggalBayar: $tanggalBayar')
                                      : null),
                              Text('Tanggal Terbuat: $tanggalTerbuat'),
                              Text(
                                  'Status Pembayaran: ${isPaid == 'true' ? 'Lunas' : 'Belum Lunas'}'),
                              Text("Jumlah Tagihan: $jumlahTagihan"),
                              Text('Kode Appointment: $kodeAppointment'),
                              const SizedBox(
                                height: 10,
                              ),
                              SizedBox(
                                width: MediaQuery.of(context).size.width,
                                child: isPaid == "false"
                                    ? ElevatedButton(
                                        style: ElevatedButton.styleFrom(
                                          primary: Colors.blue,
                                          minimumSize: Size.fromHeight(40),
                                        ),
                                        onPressed: () async {
                                          showAlertConfirmationDialog(context);
                                        },
                                        child: const Text(
                                          'Bayar',
                                          style: TextStyle(fontSize: 20),
                                        ),
                                      )
                                    : null,
                              ),
                              const SizedBox(
                                height: 10,
                              ),
                              SizedBox(
                                width: MediaQuery.of(context).size.width,
                                child: ElevatedButton(
                                  style: ElevatedButton.styleFrom(
                                    primary: Color.fromARGB(230, 206, 204, 204),
                                    minimumSize: Size.fromHeight(40),
                                  ),
                                  onPressed: () async {
                                    Navigator.pushReplacementNamed(
                                        context, '/home');
                                  },
                                  child: const Text(
                                    'Kembali',
                                    style: TextStyle(fontSize: 20),
                                  ),
                                ),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ],
                  ),
                );
              }
            },
          ),
        ));
  }
}
