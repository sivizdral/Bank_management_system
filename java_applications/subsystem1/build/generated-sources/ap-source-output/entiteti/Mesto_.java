package entiteti;

import entiteti.Filijala;
import entiteti.Komitent;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2022-02-01T00:41:32")
@StaticMetamodel(Mesto.class)
public class Mesto_ { 

    public static volatile SingularAttribute<Mesto, String> postBr;
    public static volatile SingularAttribute<Mesto, Integer> idMes;
    public static volatile SingularAttribute<Mesto, String> naziv;
    public static volatile ListAttribute<Mesto, Filijala> filijalaList;
    public static volatile ListAttribute<Mesto, Komitent> komitentList;

}