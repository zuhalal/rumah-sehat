package com.tugaskelompok.rumahsehat.admin.model;

import com.tugaskelompok.rumahsehat.user.model.UserModel;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "admin")
@PrimaryKeyJoinColumn(name = "user_uuid")
public class AdminModel extends UserModel {
}
