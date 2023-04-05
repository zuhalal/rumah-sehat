import 'package:flutter/material.dart';
import 'package:resep/models/resep.dart';

class ListObatDataTableSource extends DataTableSource {
  ListObatDataTableSource({
    required List<ListJumlah> listObatData,
  })  : _obatData = listObatData;

  final List<ListJumlah> _obatData;

  @override
  DataRow? getRow(int index) {
    final obat = _obatData[index];

    return DataRow.byIndex(
      index: index,
      cells: <DataCell>[
        DataCell(Text((index + 1).toString())),
        DataCell(Text(obat.obat.idObat)),
        DataCell(Text(obat.obat.namaObat)),
        DataCell(Text(obat.obat.harga.toString())),
        DataCell(Text(obat.kuantitas.toString())),
      ],
    );
  }

  @override
  bool get isRowCountApproximate => false;

  @override
  int get rowCount => _obatData.length;

  @override
  int get selectedRowCount => 0;
}