import 'dart:convert';
import 'package:intl/intl.dart';
import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;
import 'package:auth/providers/auth_provider.dart';

import 'package:flutter/material.dart';

import 'package:tagihan/models/list_tagihan_response.dart';

import 'package:rumahsehat_mobile/constants/api_endpoint.dart';
import 'package:rumahsehat_mobile/pages/header_drawer.dart';
import 'package:tagihan/models/tagihan.dart';
import 'package:tagihan/screens/tagihan_detail.dart';

class TagihanListScreen extends StatefulWidget {
  static const routeName = '/tagihan/list';
  const TagihanListScreen({Key? key}) : super(key: key);

  @override
  State<TagihanListScreen> createState() => _TagihanListScreenState();
}

class _TagihanListScreenState extends State<TagihanListScreen> {

  Future<List<Tagihan>?> fetchTagihanList(String? token) async {
    try {
      late Tagihan? tagihan;

      var url = Uri.parse(ApiEndpoint.tagihan);
      var response = await http.get(url, headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': 'Bearer $token',
      });

      print(response.body);

      TagihanResponse tagihanListResponse = tagihanResponseFromJson(response.body);

      List<Tagihan>? res = [];

      print("ASASASAS");

      if (response.statusCode == 200 &&
          tagihanListResponse.status == "success" && tagihanListResponse.data != null) {
            for(var d in tagihanListResponse.data) {
                tagihan = tagihanFromJson(jsonEncode(d));
                res.add(tagihan);
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
        title: const Text("Daftar Tagihan"),
      ),
      drawer: const MyHeaderDrawer(),
      body:  FutureBuilder<List<Tagihan>?>(
        future: fetchTagihanList(authProvider.jwtToken), // a previously-obtained Future<String> or null
        builder: (BuildContext context, AsyncSnapshot<List<Tagihan>?> snapshot) {
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
                        "Tagihan tidak ditemukan",
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
                  "Tagihan tidak ditemukan",
                  style: TextStyle(
                      fontSize: 16),
                ),
                SizedBox(height: 8),
              ],
            );
          } else {
            final data = snapshot.data as List<Tagihan>;

            return Padding(
              padding: const EdgeInsets.all(20),
              child:  ListView.builder(
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
                          snapshot.data![index].kode,
                          style: const TextStyle(
                            fontSize: 18.0,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        const SizedBox(height: 10),
                        Text(DateFormat('d MMM yyy HH:mm:ss').format(snapshot.data![index].tanggalTerbuat).toString()),
                        Text(snapshot.data![index].isPaid == false ? "Belum Lunas": "Lunas"),
                        const SizedBox(height: 10),
                        ElevatedButton(
                            style: ElevatedButton.styleFrom(
                              backgroundColor: Colors.blue,
                              minimumSize: const Size.fromHeight(40),
                            ),
                            onPressed: ()  {
                              Route route = MaterialPageRoute(
                                  builder: (context) => TagihanDetailScreen(kode: snapshot.data![index].kode));
                              Navigator.push(context, route);
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


