import 'package:flutter/material.dart';
import 'header_drawer.dart';


class HomePage extends StatefulWidget {
  static const routeName = '/home';
  const HomePage({Key? key}) : super(key: key);

  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage>{
  @override
  Widget build(BuildContext context){
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.blue,
        title: const Text("Rumah Sehat"),
      ), // AppBar
      drawer: const MyHeaderDrawer(),
      body: GridView.count(
        padding: const EdgeInsets.all(25),
        crossAxisCount: 2,
        children: <Widget>[
          Card(
            margin: const EdgeInsets.all(8),
            child: InkWell(
              onTap: (){Navigator.pushNamed(context, '/appointment/list');},
              splashColor: Colors.blue,
              child: Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: const <Widget>[
                  Icon(Icons.calendar_month_rounded, size: 70, color: Colors.blueGrey,),
                  Text("Lihat Jadwal Appointment", style: TextStyle(fontSize: 17.0)),
                  ],
                ),
              ),
            ),
          ), // Card
          Card(
            margin: const EdgeInsets.all(8),
            child: InkWell(
              onTap: (){Navigator.pushNamed(context, '/appointment/add');},
              splashColor: Colors.blue,
              child: Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: const <Widget>[
                  Icon(Icons.assignment_rounded, size: 70, color: Colors.blueGrey,),
                  Text("Buat Jadwal Appointment", style: TextStyle(fontSize: 17.0)),
                  ],
                ),
              ),
            ),
          ), // Card
          Card(
            margin: const EdgeInsets.all(8),
            child: InkWell(
              onTap: (){Navigator.pushNamed(context, '/profile/topup');},
              splashColor: Colors.blue,
              child: Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: const <Widget>[
                  Icon(Icons.add_card_rounded, size: 70, color: Colors.blueGrey,),
                  Text("Top Up Saldo", style: TextStyle(fontSize: 17.0)),
                  ],
                ),
              ),
            ),
          ), // Card
          Card(
            margin: const EdgeInsets.all(8),
            child: InkWell(
              onTap: (){Navigator.pushNamed(context, '/tagihan/list');},
              splashColor: Colors.blue,
              child: Center(
                child: Column(
                  mainAxisSize: MainAxisSize.min,
                  children: const <Widget>[
                  Icon(Icons.list_alt_rounded, size: 70, color: Colors.blueGrey,),
                  Text("Lihat Daftar Tagihan", style: TextStyle(fontSize: 17.0)),
                  ],
                ),
              ),
            ),
          ), // Card
        ],
      ),
    );
  }
}