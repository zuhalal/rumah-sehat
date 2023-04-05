import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:auth/providers/auth_provider.dart';
import 'package:profile/models/profile_response.dart';
import 'package:rumahsehat_mobile/widgets/drawer/main_drawer.dart';
import 'package:rumahsehat_mobile/pages/header_drawer.dart';
import 'package:profile/screens/topup_saldo.dart';


class ProfilePage extends StatefulWidget {
  static const routeName = '/profile';
  const ProfilePage({Key? key}) : super(key: key);

  @override
  State<ProfilePage> createState() => _ProfilePageState();
}

class _ProfilePageState extends State<ProfilePage> {
  @override
  Widget build(BuildContext context) {
    final authProvider = Provider.of<AuthProvider>(context);
    String? username = authProvider.getUser()?.username;
    String? email = authProvider.getUser()?.email;
    String? nama = authProvider.getUser()?.nama;
    int? saldo = authProvider.getUser()?.saldo;

    return Scaffold(
      appBar: AppBar(
        title: const Text("Profile"),
      ),
      drawer: const MyHeaderDrawer(),
      body: Center(
        child: Column(
          children: [
            SizedBox(
              height: 12,
            ),
            Container(
              color: Color(9),
              child: Text("Profile",
                  textAlign: TextAlign.center,
                  style: TextStyle(
                      fontWeight: FontWeight.bold, fontSize: 20)),
            ),
            SizedBox(
              height: 12,
            ),
            Container(
              child: Column(
                children: [
                  Text(
                    "Username",
                    style: TextStyle(fontWeight: FontWeight.w600, fontSize: 17),
                    textAlign: TextAlign.left,
                  ),
                  Text(
                    "$username",
                    textAlign: TextAlign.left,
                  ),
                ],
              ),
            ),
            SizedBox(
              height: 12,
            ),
            Container(
              child: Column(
                // ignore: prefer_const_literals_to_create_immutables, duplicate_ignore
                children: [
                  // ignore: prefer_const_constructors
                  Text(
                    "Email",
                    style: TextStyle(fontWeight: FontWeight.w600, fontSize: 17),
                    textAlign: TextAlign.left,
                  ),
                  // ignore: prefer_const_constructors
                  Text(
                    "$email",
                    textAlign: TextAlign.left,
                  ),
                ],
              ),
            ),
            SizedBox(
              height: 12,
            ),
            Container(
              child: Column(
                // ignore: prefer_const_literals_to_create_immutables, duplicate_ignore
                children: [
                  // ignore: prefer_const_constructors
                  Text(
                    "Nama",
                    style: TextStyle(fontWeight: FontWeight.w600, fontSize: 17),
                    textAlign: TextAlign.left,
                  ),
                  // ignore: prefer_const_constructors
                  Text(
                    "$nama",
                    textAlign: TextAlign.left,
                  ),
                ],
              ),
            ),
            SizedBox(
              height: 12,
            ),
            Container(
              child: Column(
                // ignore: prefer_const_literals_to_create_immutables, duplicate_ignore
                children: [
                  // ignore: prefer_const_constructors
                  Text(
                    "Saldo",
                    style: TextStyle(fontWeight: FontWeight.w600, fontSize: 17),
                    textAlign: TextAlign.left,
                  ),
                  // ignore: prefer_const_constructors
                  Text(
                    "$saldo",
                    textAlign: TextAlign.left,
                  ),
                ],
              ),
            ),
            SizedBox(
              height: 12,
            ),
            SizedBox(
              width: MediaQuery.of(context).size.width * 0.8,
              child: ElevatedButton(
                style: ElevatedButton.styleFrom(
                  backgroundColor: Colors.blue,
                  minimumSize: Size.fromHeight(40),
                ),
                onPressed: () async {
                    Navigator.pushNamed(context, '/profile/topup');
                  }
                ,
                child: const Text(
                  'Top Up Saldo',
                  style: TextStyle(fontSize: 20),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
