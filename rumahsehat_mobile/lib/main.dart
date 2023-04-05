import 'package:appointment/screens/appointment_detail.dart';
import 'package:appointment/screens/appointment.dart';
import 'package:auth/providers/auth_provider.dart';
import 'package:auth/screens/login_form.dart';
import 'package:auth/screens/regis_form.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:resep/screens/resep.dart';
import 'package:tagihan/screens/tagihan.dart';
import 'package:tagihan/screens/tagihan_detail.dart';
import 'package:rumahsehat_mobile/pages/home.dart';
import 'package:profile/screens/profile.dart';
import 'package:profile/screens/topup_saldo.dart';
import 'package:appointment/screens/appointment_sc.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
        create: (context) => AuthProvider(),
        child: MaterialApp(
            title: "Rumah Sehat",
            theme: ThemeData(primarySwatch: Colors.blue),
            home: const LoginFormScreen(),
            routes: {
              LoginFormScreen.routeName: (_) => const LoginFormScreen(),
              RegisFormScreen.routeName: (_) => const RegisFormScreen(),
              HomePage.routeName: (_) => const HomePage(),
              ProfilePage.routeName: (_) => const ProfilePage(),
              TopUpSaldoPage.routeName: (_) => const TopUpSaldoPage(),
              AppointmentScreen.routeName: (_) => const AppointmentScreen(),
              AppointmentListScreen.routeName: (_) => const AppointmentListScreen(),
              TagihanListScreen.routeName: (_)=> const TagihanListScreen()
            }));
  }
}
