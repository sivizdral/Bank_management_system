package Entiteti;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-02T20:32:44")
@StaticMetamodel(Transakcija.class)
public class Transakcija_ { 

    public static volatile SingularAttribute<Transakcija, String> vrsta;
    public static volatile SingularAttribute<Transakcija, Integer> iznos;
    public static volatile SingularAttribute<Transakcija, Integer> redniBrSa;
    public static volatile SingularAttribute<Transakcija, Date> datumVreme;
    public static volatile SingularAttribute<Transakcija, String> svrha;
    public static volatile SingularAttribute<Transakcija, Integer> idRacSa;
    public static volatile SingularAttribute<Transakcija, Integer> idFil;
    public static volatile SingularAttribute<Transakcija, Integer> redniBrKa;
    public static volatile SingularAttribute<Transakcija, Integer> idRacKa;
    public static volatile SingularAttribute<Transakcija, Integer> idTra;

}