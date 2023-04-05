import 'dart:convert';
import 'package:auth/providers/auth_provider.dart';
import 'package:flutter/material.dart';
import 'package:flutter_datetime_picker/flutter_datetime_picker.dart';
import 'package:appointment/models/dokter.dart';
import 'package:http/http.dart';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:rumahsehat_mobile/constants/api_endpoint.dart';
import 'package:rumahsehat_mobile/pages/header_drawer.dart';
import 'package:http/http.dart' as http;

import '../models/appointment_response.dart';

class AppointmentScreen extends StatefulWidget {
  static const routeName = '/appointment/add';
  const AppointmentScreen({Key? key}) : super(key: key);

  @override
  State<AppointmentScreen> createState() => _AppointmentScreenState();
}

class _AppointmentScreenState extends State<AppointmentScreen> {

  final _formKey = GlobalKey<FormState>();
  TextEditingController dateTimeController = TextEditingController();
  List<Dokter> listDokter = [];
  Set<String> setUUID = {};
  bool checkSet = false;

  DateTime? aptDateTime;
  Dokter? dokterChosen;


  showLoading(BuildContext context) {
    return showDialog(
        context: context,
        barrierDismissible: false,
        builder: (BuildContext context) {
          return const Center(
            child: CircularProgressIndicator(),
          );
        }
    );
  }

  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);
    String? jwtToken4 = authProvider.jwtToken;

    Future<List<Dokter>?> fetchListDokter(String? token) async {
      try {
        var url = Uri.parse("${ApiEndpoint.baseUrl}/dokter/list-dokter");
        var response = await get(url, headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Authorization': 'Bearer $token',
        });
        var jsonData = jsonDecode(response.body)['data'];
        for (var dok in jsonData) {
          if(!setUUID.contains(dok['uuid'])) {
            Dokter dokter = Dokter(
                uuid: dok['uuid'], nama: dok['nama'], tarif: dok['tarif']);
            listDokter.add(dokter);
            setUUID.add(dok['uuid']);
          }
          else{
            continue;
          }
        }
        if(listDokter.isNotEmpty && checkSet == false){
          dokterChosen = listDokter[0];
          checkSet = true;
        }
        return listDokter;
      } catch (e) {
        return null;
      }
    }

    Future<PostAppointmentResponse?> create_appointment(String? token) async {
      try {
        var url = Uri.parse(ApiEndpoint.addAppointment);
        var response = await http.post(url, body: json.encode({
          "waktuAwal": aptDateTime?.toIso8601String(),
          "dokterId": dokterChosen?.uuid,
        }), headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Authorization': 'Bearer $token',
        });

        PostAppointmentResponse postAppointmentResponse = postAppointmentResponseFromJson(
            response.body);
        return postAppointmentResponse;
      }
      catch (e) {
        return null;
      }
    }

    showSuccessDialog(BuildContext context) {
      // set up the button
      Widget okButton = ElevatedButton(
        style: ElevatedButton.styleFrom(
          textStyle: const TextStyle(fontSize: 20),
          padding: const EdgeInsets.fromLTRB(12.0, 8.0, 12.0, 8.0),
          shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(8.0)
          ),
        ),
        onPressed: () => Navigator.pop(context),
        child: const Text('OK'),
      );

      AlertDialog alert = AlertDialog(
        title: const Text("Berhasil membuat appointment!"),
        content: const Text("Appointment baru telah ditambahkan."),
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
          onPressed: () => Navigator.pop(context),
          child: const Text('OK')
      );

      AlertDialog alert = AlertDialog(
        title: const Text("Gagal membuat Appointment!"),
        content: const Text("Jadwal tersebut sudah diisi pasien lain, silakan cari jadwal baru."),
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
          onPressed: () => Navigator.pop(context),
          child: const Text('Batal')
      );

      Widget confirmationButton = ElevatedButton(
          style: ElevatedButton.styleFrom(
              textStyle:
              const TextStyle(fontSize: 20),
              padding: const EdgeInsets.fromLTRB(12.0, 8.0, 12.0, 8.0),
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(8.0))),
          onPressed: () async {
            PostAppointmentResponse? response = await create_appointment(authProvider.jwtToken);
            if (response?.statusCode == 200 && response?.status == "success") {
              await authProvider.fetchUserDetails();
              Navigator.pushNamed(context, '/home');
              showSuccessDialog(context);
            } else {
              showFailedDialog(context);
            }
          },
          child: const Text("Konfirmasi")
      );

      AlertDialog alert = AlertDialog(
        title: const Text("Buat Appointment Baru"),
        content: const Text(
            "Pastikan data yang diisikan sudah benar"),
        actions: [
          cancelButton,
          confirmationButton
        ],
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
          title: const Text("Membuat Appointment"),
        ),
        drawer: const MyHeaderDrawer(),
        body: SingleChildScrollView(scrollDirection: Axis.vertical,
          child: FutureBuilder<List<Dokter>?>(
              future: fetchListDokter(authProvider.jwtToken),
              builder: (BuildContext context,
                  AsyncSnapshot<List<Dokter>?> snapshot) {
                if (snapshot.data == null || !snapshot.hasData) {
                  if (snapshot.connectionState == ConnectionState.done) {
                    return Padding(
                      padding: EdgeInsets.all(12),
                      child: Center(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: [
                            const Text(
                              "Belum ada dokter yang tersedia.",
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
                                child: const Text('Kembali ke Beranda',
                                  style: TextStyle(fontSize: 16,),
                                )
                            ),
                          ],
                        ),
                      ),
                    );
                  }
                  return const Center(child: CircularProgressIndicator());
                }
                else {
                  return Form(
                      key: _formKey,
                      child: Padding(
                          padding: const EdgeInsets.all(12),
                          child: Center(
                              child: Column(
                                  mainAxisAlignment: MainAxisAlignment.center,
                                  crossAxisAlignment: CrossAxisAlignment.center,
                                  children: <Widget>[
                                    const Text("Pilih Dokter: ",
                                      style: TextStyle(fontSize: 24),
                                    ),
                                    const SizedBox(height: 10),
                                    DropdownButton<Dokter>(
                                      isExpanded: true,
                                      icon: const Icon(
                                          Icons.keyboard_arrow_down),
                                      value: dokterChosen,
                                      onChanged: (Dokter? newValue) {
                                        setState(() {
                                          dokterChosen = newValue!;
                                        });
                                      },
                                      items: listDokter.map((Dokter dokter) {
                                        return DropdownMenuItem(
                                            value: dokter,
                                            child: Text(
                                                '${dokter.nama} - ${dokter.tarif}')
                                        );
                                      }).toList(),
                                    ),
                                    const Text("Pilih Waktu Appointment: "),
                                    TextFormField(
                                      controller: dateTimeController,
                                      validator: (value) {
                                        if (value == null || value.isEmpty) {
                                          return 'Pilih waktu appointment!';
                                        }
                                        return null;
                                      },
                                      onTap: () async {
                                        FocusScope.of(context).requestFocus(
                                            FocusNode());
                                        await DatePicker.showDateTimePicker(
                                          context,
                                          showTitleActions: true,
                                          minTime: DateTime.now(),
                                          currentTime: DateTime.now(),
                                          locale: LocaleType.en,
                                          onConfirm: (date) {
                                            dateTimeController.text =
                                                DateFormat('dd-MM-yyyy hh:mm')
                                                    .format(date);
                                            aptDateTime = date;
                                          },
                                        );
                                      },
                                    ),
                                    const SizedBox(height: 8),
                                    ElevatedButton(
                                        style: ElevatedButton.styleFrom(
                                          backgroundColor: Colors.blue,
                                          minimumSize: Size.fromHeight(40),
                                        ),
                                        onPressed: () {
                                          if (_formKey.currentState!
                                              .validate()) {
                                            showAlertConfirmationDialog(
                                                context);
                                            //create_appointment();
                                          }
                                        },
                                        child: const Text('Konfirmasi',
                                          style: TextStyle(fontSize: 16,),
                                        )
                                    ),
                                  ]
                              ))));
                }
              }
          ),
        )
    );
  }
}
