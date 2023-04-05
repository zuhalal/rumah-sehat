import 'dart:convert';
import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;
import 'package:auth/providers/auth_provider.dart';

import 'package:flutter/material.dart';

import 'package:resep/models/resep.dart';
import 'package:resep/models/resep_detail_response.dart';
import 'package:resep/source/list_obat_data_table_source.dart';

import 'package:rumahsehat_mobile/constants/api_endpoint.dart';
import 'package:rumahsehat_mobile/widgets/drawer/main_drawer.dart';

class ResepDetailScreen extends StatefulWidget {
  static const routeName = '/resep/detail';
  const ResepDetailScreen({Key? key, required this.id}) : super(key: key);
  final int id;

  @override
  State<ResepDetailScreen> createState() => _ResepDetailScreenState();
}

class _ResepDetailScreenState extends State<ResepDetailScreen> {

  Future<Resep?> fetchResepDetails(String? token) async {
    try {
      late Resep? resep;

      var url = Uri.parse("${ApiEndpoint.resep}/${widget.id}");
      var response = await http.get(url, headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': 'Bearer $token',
      });

      ResepDetailResponse resepDetailResponse = resepDetailResponseFromJson(response.body);

      if (response.statusCode == 200 &&
          resepDetailResponse.status == "success") {
        resep = resepFromJson(json.encode(resepDetailResponse.data));
      } else {
        resep = null;
      }

      return resep;
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
        title: const Text("Detail Resep"),
      ),
      drawer: const MainDrawer(),
      body:  SingleChildScrollView(
        child: FutureBuilder<Resep?>(
          future: fetchResepDetails(authProvider.jwtToken), // a previously-obtained Future<String> or null
          builder: (BuildContext context, AsyncSnapshot<Resep?> snapshot) {
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
                          "Detail Resep tidak ditemukan",
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
                    "Resep detail tidak ditemukan",
                    style: TextStyle(
                        fontSize: 16),
                  ),
                  SizedBox(height: 8),
                ],
              );
            } else {
              final data = snapshot.data as Resep;
              final id = data.id.toString();
              final isDone = data.isDone.toString();
              final namaDokter = data.kodeAppointment.dokter.nama.toString();
              final namaPasien = data.kodeAppointment.pasien.nama.toString();
              final namaApoteker = data.confirmerUuid?.nama?.toString() ?? "Belum ada";
              final listObat = data.listJumlah;

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
                              child: Text("Detail Resep",   style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                            ),
                            const SizedBox(height: 8),
                            Text('ID: $id'),
                            Text('Nama Dokter: $namaDokter'),
                            Text('Nama Pasien: $namaPasien'),
                            Text('Status Resep: ${isDone == 'true' ? 'Selesai' : 'Belum Selesai'}'),
                            Text('Nama Apoteker Pengonfirmasi: $namaApoteker'),
                            Text("Jumlah Obat: ${listObat.length}"),
                          ],
                        ),
                      ),
                    ),
                    const SizedBox(height: 20,),
                    Card(
                      elevation: 4,
                      shadowColor: Colors.black,
                      shape: RoundedRectangleBorder(
                        side: const BorderSide(width: 1, color: Colors.white),
                        borderRadius: BorderRadius.circular(8),
                      ),
                      child: Padding(
                        padding: const EdgeInsets.all(20),
                        child: Column(
                          children: [
                            const Text("Daftar Obat", style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)),
                            SizedBox(
                              width: MediaQuery.of(context).size.width * 0.9,
                              child: Align(
                                  child: Column(
                                    children: [
                                      Padding(
                                        padding: const EdgeInsets.all(4),
                                        child: SizedBox(
                                          child:
                                          PaginatedDataTable(
                                            source: ListObatDataTableSource(listObatData: listObat),
                                            rowsPerPage: 5,
                                            columns: const [
                                              DataColumn(label: Text(
                                                  'No',
                                                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)
                                              )),
                                              DataColumn(label: Text(
                                                  'ID',
                                                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)
                                              )),
                                              DataColumn(label: Text(
                                                  'Nama',
                                                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)
                                              )),
                                              DataColumn(label: Text(
                                                  'Harga',
                                                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)
                                              )),
                                              DataColumn(label: Text(
                                                  'Kuantitas',
                                                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold)
                                              )),
                                            ],
                                          ),
                                        ),
                                      )
                                    ],
                                  )
                              ),
                            )
                          ],
                        ),
                      )
                    )
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

