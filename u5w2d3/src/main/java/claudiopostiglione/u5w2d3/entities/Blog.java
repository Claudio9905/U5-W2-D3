package claudiopostiglione.u5w2d3.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "blog")
@NoArgsConstructor
@Data
public class Blog {

    //Attributi
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;
    @Column(name = "Categoria")
    private String categoria;
    @Column(name = "Titolo")
    private String titolo;
    @Column(name = "Cover_url")
    private String cover;
    @Column(name = "Contenuto")
    private String contenuto;
    @Column(name = "Tempo_Di_Lettura")
    private int tempoDiLettura;


    @ManyToOne
    @JoinColumn(name = "author")
    private Autore author;

    //Costruttori
    public Blog(String categoria, String titolo, String cover, String contenuto, int tempoDiLettura) {
        this.categoria = categoria;
        this.titolo = titolo;
        this.cover = cover;
        this.contenuto = contenuto;
        this.tempoDiLettura = tempoDiLettura;
    }
}
