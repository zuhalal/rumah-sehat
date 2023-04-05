package com.tugaskelompok.rumahsehat.apoteker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tugaskelompok.rumahsehat.resep.model.ResepModel;
import com.tugaskelompok.rumahsehat.user.model.UserModel;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "apoteker")
@PrimaryKeyJoinColumn(name = "user_uuid")
public class ApotekerModel extends UserModel  {
    @OneToMany(mappedBy = "confirmerUUID", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ResepModel> listResep;
}
