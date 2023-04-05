package com.tugaskelompok.rumahsehat.obat.repository;

import com.tugaskelompok.rumahsehat.obat.projections.DailyObatIncomeProjections;
import com.tugaskelompok.rumahsehat.obat.projections.MonthlyObatIncomeProjections;
import com.tugaskelompok.rumahsehat.obat.projections.ObatKuantitasProjections;
import com.tugaskelompok.rumahsehat.obat.projections.ObatTotalPendapatanProjections;
import com.tugaskelompok.rumahsehat.obat.model.ObatModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ObatDb extends JpaRepository<ObatModel, String> {
    Optional<ObatModel> findById(String id);

    List<ObatModel> findAllByOrderByNamaObatAsc();

    @Query("SELECT t.tanggalBayar as tanggal, SUM(j.kuantitas*o.harga) as pemasukan from ResepModel r, TagihanModel t, AppointmentModel a, ObatModel o, JumlahModel j where r.kodeAppointment.kode=a.kode and t.kodeAppointment.kode=a.kode and j.resep.id = r.id and o.idObat =j.obat.idObat and t.isPaid=true and YEAR(t.tanggalBayar) = YEAR(CURRENT_DATE) GROUP BY date_format(t.tanggalBayar, '%M %Y')")
    Optional<List<MonthlyObatIncomeProjections>> findMonthlyObatIncomeThisYear();

    @Query("SELECT new com.tugaskelompok.rumahsehat.obat.projections.ObatKuantitasProjections(o, SUM(j.kuantitas)) from ResepModel AS r, TagihanModel AS t, AppointmentModel AS a, ObatModel AS o, JumlahModel AS j WHERE r.kodeAppointment.kode=a.kode and t.kodeAppointment.kode=a.kode and j.resep.id = r.id and o.namaObat = :namaObat and o.idObat = j.obat.idObat and t.isPaid = true GROUP BY o.namaObat")
    Optional<ObatKuantitasProjections> findKuantitasObat(@Param("namaObat") String namaObat);

    @Query("SELECT new com.tugaskelompok.rumahsehat.obat.projections.ObatTotalPendapatanProjections(o, SUM(j.kuantitas*o.harga)) from ResepModel AS r, TagihanModel AS t, AppointmentModel AS a, ObatModel AS o, JumlahModel AS j WHERE r.kodeAppointment.kode=a.kode and t.kodeAppointment.kode=a.kode and j.resep.id = r.id and o.namaObat = :namaObat and o.idObat = j.obat.idObat and t.isPaid = true GROUP BY o.namaObat")
    Optional<ObatTotalPendapatanProjections> findPendapatanObat(@Param("namaObat") String namaObat);

    @Query("SELECT t.tanggalBayar as tanggal, SUM(j.kuantitas*o.harga) as pemasukan from ResepModel r, TagihanModel t, AppointmentModel a, ObatModel o, JumlahModel j where r.kodeAppointment.kode=a.kode and t.kodeAppointment.kode=a.kode and j.resep.id = r.id and o.idObat = :idObat and o.idObat =j.obat.idObat and t.isPaid=true and YEAR(t.tanggalBayar) = YEAR(CURRENT_DATE) GROUP BY o.idObat, date_format(t.tanggalBayar, '%M %Y')")
    Optional<List<MonthlyObatIncomeProjections>> findMonthlyIncomeOfSpecificObat(@Param("idObat") String idObat);

    @Query("SELECT t.tanggalBayar as tanggal, SUM(j.kuantitas*o.harga) as pemasukan from ResepModel r, TagihanModel t, AppointmentModel a, ObatModel o, JumlahModel j where r.kodeAppointment.kode=a.kode and t.kodeAppointment.kode=a.kode and j.resep.id = r.id and o.idObat = :idObat and o.idObat = j.obat.idObat and t.isPaid=true and YEAR(t.tanggalBayar) = YEAR(CURRENT_DATE) GROUP BY o.namaObat, date_format(t.tanggalBayar, '%D %M %Y')")
    Optional<List<DailyObatIncomeProjections>> findDailyIncomeOfObat(@Param("idObat") String idObat);
}
