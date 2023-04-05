import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;
import 'package:auth/providers/auth_provider.dart';

import 'package:flutter/material.dart';

import 'package:appointment/models/appointment_detail_response.dart';

import 'package:rumahsehat_mobile/constants/api_endpoint.dart';

import 'package:rumahsehat_mobile/widgets/drawer/main_drawer.dart';
import 'package:resep/screens/resep.dart';

class AppointmentDetailScreen extends StatefulWidget {
  static const routeName = '/appointment/detail';
  const AppointmentDetailScreen({Key? key, required this.kode}) : super(key: key);
  final String kode;

  @override
  State<AppointmentDetailScreen> createState() => _AppointmentDetailScreenState();
}

class _AppointmentDetailScreenState extends State<AppointmentDetailScreen> {

  Future<DetailAppointmentResponse?> fetchAppointmentDetails(String? token) async {
    try {
      late DetailAppointmentResponse? appointment;

      var url = Uri.parse("${ApiEndpoint.baseUrl}/appointment/detail/${widget.kode}");

      var response = await http.get(url, headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': 'Bearer $token',
      });
      // DetailAppointmentResponse appointmentDetailResponse = detailAppointmentResponseFromJson(response.body);

      if (response.statusCode == 200) {
        appointment = detailAppointmentResponseFromJson(response.body);
      } else {
        appointment = null;
      }

      return appointment;
    } catch (e) {
      print(e.toString());
      return null;
    }
  }

  showFailedDialog(BuildContext context) {  // set up the button
    Widget okButton = TextButton(
        onPressed: ()=>Navigator.pop(context),
        child: const Text('OK')
    );

    AlertDialog alert = AlertDialog(
      title: const Text("Tidak ada resep"),
      content: const Text("Appointment anda tidak memiliki resep."),
      actions: [
        okButton,
      ],
    );  // show the dialog
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return alert;
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);
    return Scaffold(
        appBar: AppBar(
          title: const Text("Detail Appointment"),
        ),
        drawer: const MainDrawer(),
        body:  SingleChildScrollView(
          child: FutureBuilder<DetailAppointmentResponse?>(
            future: fetchAppointmentDetails(authProvider.jwtToken), // a previously-obtained Future<String> or null
            builder: (BuildContext context, AsyncSnapshot<DetailAppointmentResponse?> snapshot) {
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
                            "Detail Appointment tidak ditemukan",
                            style: TextStyle(
                                fontSize: 16),
                          ),
                          const SizedBox(height: 8),
                          ElevatedButton(
                              style: ElevatedButton.styleFrom(
                                backgroundColor: Colors.blue,
                                minimumSize: Size.fromHeight(40),
                              ),
                              onPressed: ()  {
                                Navigator.pushNamed(context, '/home');
                              },
                              child: const Text(
                                'Kembali ke Beranda',
                                style: TextStyle(fontSize: 16,
                                ),
                              )
                          ),
                        ],
                      ),
                    ),
                  );
                }
                return const Center(
                    child: CircularProgressIndicator()
                );
              }

              if (!snapshot.hasData) {
                return Column(
                  children: const [
                    Text(
                      "Appointment detail tidak ditemukan",
                      style: TextStyle(
                          fontSize: 16),
                    ),
                    SizedBox(height: 8),
                  ],
                );
              } else {
                final data = snapshot.data as DetailAppointmentResponse;
                final kode = data.kode.toString();
                final waktuAwal = data.waktuAwal.toString();
                final statusAppointment = data.statusAppointment.toString();
                final dokter = data.dokter.toString();
                final pasien = data.pasien.toString();
                final resep = data?.resep;

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
                          height: 250,
                          padding: const EdgeInsets.all(12),
                          width: double.maxFinite,
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              const Center(
                                child: Text("Detail Appointment",   style: TextStyle(fontSize: 19, fontWeight: FontWeight.bold)),
                              ),
                              const SizedBox(height: 15),
                              Text('Kode Appointment: $kode',
                                textAlign: TextAlign.center,),
                              SizedBox(
                                height: 12,
                              ),
                              Text('Waktu Awal: $waktuAwal',
                                textAlign: TextAlign.center,),
                              SizedBox(
                                height: 12,
                              ),
                              Text('Status Appointment: ${statusAppointment == 'true' ? 'Selesai' : 'Belum Selesai'}',
                                textAlign: TextAlign.center,),
                              SizedBox(
                                height: 12,
                              ),
                              Text('Nama Dokter: $dokter',
                                textAlign: TextAlign.center,),
                              SizedBox(
                                height: 12,
                              ),
                              Text('Nama Pasien: $pasien',
                                textAlign: TextAlign.center,),
                              SizedBox(
                                height: 15,
                              ),
                              // Text('Nama Apoteker Pengonfirmasi: $namaApoteker'),
                              // Text("Jumlah Obat: ${listObat.length}"),
                              SizedBox(
                                height: 15,
                                width: MediaQuery.of(context).size.width * 0.5,
                                child: ElevatedButton(
                                  style: ElevatedButton.styleFrom(
                                    backgroundColor: Colors.blue,
                                    minimumSize: Size.fromHeight(40),
                                  ),
                                  onPressed: () async {
                                    if (resep != null) {
                                      Route route = MaterialPageRoute(
                                          builder: (context) => ResepDetailScreen(
                                              id: resep));
                                      Navigator.push(context, route);
                                    } else {
                                      showFailedDialog(context);
                                    }
                                  },
                                  child: const Text(
                                    'Detail Resep',
                                    style: TextStyle(fontSize: 15),
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
        )
    );
  }
}
