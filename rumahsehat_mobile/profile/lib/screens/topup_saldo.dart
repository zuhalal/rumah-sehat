import 'dart:convert';

import 'package:auth/providers/auth_provider.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:profile/models/topup_response.dart';
import 'package:provider/provider.dart';
import 'package:rumahsehat_mobile/constants/api_endpoint.dart';
import 'package:rumahsehat_mobile/widgets/drawer/main_drawer.dart';
import 'package:rumahsehat_mobile/pages/header_drawer.dart';
import 'package:http/http.dart' as http;

class TopUpSaldoPage extends StatefulWidget {
  static const routeName = '/profile/topup';
  const TopUpSaldoPage({Key? key}) : super(key: key);

  @override
  State<TopUpSaldoPage> createState() => _TopUpSaldoPageState();
}

class _TopUpSaldoPageState extends State<TopUpSaldoPage> {
  final _formKey = GlobalKey<FormState>();

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

  TextEditingController nominalController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);

    String? jwtToken = authProvider.getJwtToken();

    Future<TopUpResponse?> topUpSaldo(int nominal) async {
      try {
        var url = Uri.parse(ApiEndpoint.topUpSaldo);

        var response = await http.post(url,body: json.encode({ "nominal": nominal }), headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Authorization': 'Bearer $jwtToken',
        });

        TopUpResponse topUpResponse = topUpResponseFromJson(response.body);

        return topUpResponse;
      }
      catch (e) {
        return null;
      }
    }

    showSuccessDialog(BuildContext context) {  // set up the button
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
        title: const Text("Top Up Berhasil!"),
        content: const Text("Saldo berhasil bertambah."),
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

    showFailedDialog(BuildContext context) {  // set up the button
      Widget okButton = TextButton(
          onPressed: ()=>Navigator.pop(context),
          child: const Text('OK')
      );

      AlertDialog alert = AlertDialog(
        title: const Text("Top Up Gagal!"),
        content: const Text("Top up saldo gagal, silahkan coba lagi nanti."),
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

    showAlertConfirmationDialog(BuildContext context) {  // set up the buttons
      Widget cancelButton =  TextButton(
          onPressed: ()=>Navigator.pop(context),
          child: const Text('Batal')
      );

      Widget confirmationButton = ElevatedButton(
          style: ElevatedButton.styleFrom(
              textStyle:
              const TextStyle(fontSize: 20),
              padding: const EdgeInsets.fromLTRB(12.0, 8.0, 12.0, 8.0),
              shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(8.0))),
          onPressed: () async {
            TopUpResponse? response = await topUpSaldo(int.parse(nominalController.text));

            if (response?.statusCode == 200 && response?.status == "success") {
              await authProvider.fetchUserDetails();
              Navigator.pushNamed(context, '/profile');
              showSuccessDialog(context);
            } else {
              showFailedDialog(context);
            }
          },
          child: const Text("Konfirmasi")
      );

      AlertDialog alert = AlertDialog(
        title: const Text("Top Up Saldo"),
        content: const Text("Pastikan jumlah nominal yang dimasukkan sudah benar"),
        actions: [
          cancelButton,
          confirmationButton
        ],
      );  // show the dialog
      showDialog(
        context: context,
        builder: (BuildContext context) {
          return alert;
        },
      );
    }

    return Scaffold(
      appBar: AppBar(
        title: const Text("Top Up Saldo"),
      ),
        drawer: const MyHeaderDrawer(),
      body: Center(
        child: Padding(
          padding: EdgeInsets.all(20),
          child:  Column(
            children: [
              TextFormField(
                decoration: const InputDecoration(labelText: "Masukan Nominal"),
                keyboardType: TextInputType.number,
                inputFormatters: <TextInputFormatter>[
                  FilteringTextInputFormatter.digitsOnly
                ],
                controller: nominalController,
                validator: (value) {
                  if (int.parse(value!) <= 0) {
                    return "Jumlah nominal yang dimasukkan harus bilangan positif";
                  }
                  return null;
                },
              ),
              const SizedBox(height: 20),
              SizedBox(
                width: MediaQuery.of(context).size.width * 1,
                child: ElevatedButton(
                  style: ElevatedButton.styleFrom(
                    primary: Colors.blue,
                    minimumSize: const Size.fromHeight(40),
                  ),
                  onPressed: () async {
                    if (nominalController.text == '' || int.parse(nominalController.text) <= 0) {
                      ScaffoldMessenger.of(context).showSnackBar(
                        const SnackBar(content: Text('Masukkan jumlah nominal lebih dari 0')),
                      );
                    } else {
                      showAlertConfirmationDialog(context);
                    }
                  },
                  child: const Text(
                    'Konfirmasi',
                    style: TextStyle(fontSize: 20),
                  ),
                ),
              ),
            ],
          ),
        )
      ),
    );
  }
}
