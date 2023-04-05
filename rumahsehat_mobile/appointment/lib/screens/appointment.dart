import 'dart:convert';
import 'package:appointment/screens/appointment_detail.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;
import 'package:auth/providers/auth_provider.dart';

import 'package:flutter/material.dart';

import 'package:appointment/models/list_appointment_response.dart';

import 'package:rumahsehat_mobile/constants/api_endpoint.dart';
import 'package:rumahsehat_mobile/pages/header_drawer.dart';

class AppointmentListScreen extends StatefulWidget {
  static const routeName = '/appointment/list';
  const AppointmentListScreen({Key? key}) : super(key: key);

  @override
  State<AppointmentListScreen> createState() => _AppointmentListScreenState();
}

class _AppointmentListScreenState extends State<AppointmentListScreen> {

  Future<List<Appointment>?> fetchAppointmentDetails(String? token) async {
    try {
      late Appointment? appointment;

      var url = Uri.parse(ApiEndpoint.appointment);
      var response = await http.get(url, headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': 'Bearer $token',
      });

      AppointmentListResponse appointmentListResponse = appointmentListResponseFromJson(response.body);

      List<Appointment>? res = [];

      if (response.statusCode == 200 &&
          appointmentListResponse.status == "success" && appointmentListResponse.data != null) {
            for(var d in appointmentListResponse.data) {
                appointment = appointmentFromJson(jsonEncode(d));
                res.add(appointment);
            }
      } else {
        res = null;
      }

      return res;
    } catch (e) {
      print(e.toString());
      return null;
    }
  }

  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);
    return Scaffold(
      appBar: AppBar(
        title: const Text("Daftar Appointment"),
      ),
      drawer: const MyHeaderDrawer(),
      body:  FutureBuilder<List<Appointment>?>(
        future: fetchAppointmentDetails(authProvider.jwtToken), // a previously-obtained Future<String> or null
        builder: (BuildContext context, AsyncSnapshot<List<Appointment>?> snapshot) {
          if (snapshot.data == null) {
            if (snapshot.connectionState == ConnectionState.done) {
              return Padding(
                padding: const EdgeInsets.all(12),
                child: Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      const Text(
                        "Appointment tidak ditemukan",
                        style: TextStyle(
                            fontSize: 16),
                      ),
                      const SizedBox(height: 8),
                      ElevatedButton(
                          style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.blue,
                            minimumSize: const Size.fromHeight(40),
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
                  "Appointment tidak ditemukan",
                  style: TextStyle(
                      fontSize: 16),
                ),
                SizedBox(height: 8),
              ],
            );
          } else {
            final data = snapshot.data as List<Appointment>;

            return Padding(
              padding: const EdgeInsets.all(20),
              child: ListView.builder(
                  shrinkWrap: true,
                  itemCount: snapshot.data!.length,
                  itemBuilder: (_, index)=> Container(
                    margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
                    padding: const EdgeInsets.all(20.0),
                    decoration: BoxDecoration(
                        color:Colors.white,
                        borderRadius: BorderRadius.circular(15.0),
                        boxShadow: const [
                          BoxShadow(
                              color: Colors.black,
                              blurRadius: 2.0
                          )
                        ]
                    ),
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.start,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          snapshot.data![index].dokter.nama,
                          style: const TextStyle(
                            fontSize: 18.0,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 10),
                        Text(DateFormat('d MMM yyy HH:mm:ss').format(snapshot.data![index].waktuAwal).toString()),
                        Text(snapshot.data![index].isDone == false ? "Belum Selesai": "Selesai"),
                        const SizedBox(height: 10),
                        ElevatedButton(
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.blue,
                              minimumSize: const Size.fromHeight(40),
                            ),
                            onPressed: () {
                              Route route = MaterialPageRoute(
                                  builder: (context) => AppointmentDetailScreen(
                                    kode: snapshot.data![index].kode,));
                              Navigator.push(context, route);
                              // Navigator.pushNamed(context, '/appointment/detail'); //arahin ke detail apt
                            },
                            child: const Text(
                              'Detail',
                              style: TextStyle(fontSize: 16,
                              ),
                            )
                        ),
                      ],
                    ),
                  )
              ),
            );
          }
        },
      ),
    );
  }
}


