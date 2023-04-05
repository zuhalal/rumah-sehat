import 'package:flutter/material.dart';
import 'package:rumahsehat_mobile/widgets/drawer/drawer_list_tile.dart';
import 'package:auth/providers/auth_provider.dart';
import 'package:provider/provider.dart';

class MainDrawer extends StatelessWidget {
  const MainDrawer({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: Column(
        children: <Widget>[
          Container(
              height: 160,
              width: double.infinity,
              padding: const EdgeInsets.all(32),
              alignment: Alignment.centerLeft,
              color: const Color.fromRGBO(89, 165, 216, 1),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  const Text(
                    "Rumah Sehat",
                    style: TextStyle(
                      fontWeight: FontWeight.w700,
                      fontSize: 32,
                      color: Colors.white,
                    ),
                  ),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: const [
                      Text(
                        "Kelompok 69",
                        style: TextStyle(
                          fontWeight: FontWeight.w700,
                          fontSize: 20,
                          color: Colors.white,
                        ),
                      ),
                    ],
                  )
                ],
              )),
          DrawerListTile(
              title: 'Beranda',
              iconData: Icons.home,
              onTilePressed: () {
                Navigator.pushNamed(context, '/home');
              }),
          DrawerListTile(
              title: 'Profile',
              iconData: Icons.person,
              onTilePressed: () {
                Navigator.pushNamed(context, '/profile');
              }),
          DrawerListTile(
              title: 'Logout',
              iconData: Icons.logout,
              onTilePressed: () async {
                final authProvider =
                    Provider.of<AuthProvider>(context, listen: false);
                await authProvider.logout();
                Navigator.pushReplacementNamed(context, '/login');
              }),
          DrawerListTile(
              title: 'Detail Resep Dummy',
              iconData: Icons.receipt,
              onTilePressed: () {
                Navigator.pushReplacementNamed(context, '/resep/detail');
              }),
          DrawerListTile(
              title: 'Detail Tagihan Dummy',
              iconData: Icons.receipt,
              onTilePressed: () {
                Navigator.pushReplacementNamed(context, '/tagihan/detail');
              }),
          DrawerListTile(
              title: 'Buat Appointment Baru',
              iconData: Icons.receipt,
              onTilePressed: () {
                Navigator.pushReplacementNamed(context, '/appointment/add');
              }),
          DrawerListTile(
              title: 'Detail Appointment Dummy',
              iconData: Icons.receipt,
              onTilePressed: () {
                Navigator.pushReplacementNamed(context, '/appointment/detail');
              }),
        ],
      ),
    );
  }
}
