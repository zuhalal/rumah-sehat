import 'package:flutter/material.dart';
import 'package:auth/providers/auth_provider.dart';
import 'package:provider/provider.dart';

class MyHeaderDrawer extends StatefulWidget {
  const MyHeaderDrawer({Key? key}) : super(key: key);

  @override
  _MyHeaderDrawerState createState() => _MyHeaderDrawerState();
}

class _MyHeaderDrawerState extends State<MyHeaderDrawer> {
  @override
  Widget build(BuildContext context) {
    var mainAxisAlignment;
    return Drawer(
            child: SingleChildScrollView(
              child: Column(
               children: [
                Container(
              height: 160,
              width: double.infinity,
              padding: const EdgeInsets.all(16),
              alignment: Alignment.centerLeft,
              color: const Color.fromRGBO(89, 165, 216, 1),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                   Container(
            margin: const EdgeInsets.only(bottom: 10.0),
            decoration: const BoxDecoration(
              shape: BoxShape.rectangle,
            ),
          ),
          const Text("Rumah Sehat", style: TextStyle(color: Colors.white, fontSize: 20),),
          const Text("Selamat datang di Rumah Sehat", style: TextStyle(color: Colors.white, fontSize: 14),),
                ],
              )
              ),
              ListTile(
                leading: const Icon(Icons.account_circle),
                title: const Text("Profile"),
                onTap: () {Navigator.pushNamed(context, '/profile');},
              ),
              ListTile(
                leading: const Icon(Icons.home),
                title: const Text("Beranda"),
                onTap: () {Navigator.pushNamed(context, '/home');},
              ),
              ListTile(
                leading: const Icon(Icons.calendar_month_rounded),
                title: const Text("Lihat Jadwal Appointment"),
                onTap: () {Navigator.pushNamed(context, '/appointment/list');},
              ),
              ListTile(
                leading: const Icon(Icons.assignment_rounded),
                title: const Text("Buat Jadwal Appointment"),
                onTap: () {Navigator.pushNamed(context, '/appointment/add');},
              ),
              ListTile(
                leading: const Icon(Icons.add_card_rounded),
                title: const Text("Top Up Saldo"),
                onTap: () {Navigator.pushNamed(context, '/profile/topup');},
              ),
              ListTile(
                leading: const Icon(Icons.list_alt_rounded),
                title: const Text("Lihat Daftar Tagihan"),
                onTap: () {Navigator.pushNamed(context, '/tagihan/list');},
              ),
              ListTile(
                leading: const Icon(Icons.logout),
                title: const Text("Log Out"),
                onTap: () async {
                  final authProvider = 
                      Provider.of<AuthProvider>(context, listen: false);
                  await authProvider.logout();
                  Navigator.pushReplacementNamed(context, '/login');
                }),
            ]),
        ),
      );
  }
}