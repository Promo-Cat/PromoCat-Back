package org.promocat.promocat.data_entities.admin;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.promocat.promocat.data_entities.abstract_account.AbstractAccount;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(of = {}, callSuper = true)
@Entity
@Table(name = "admin")
@Data
public class Admin extends AbstractAccount {

}
