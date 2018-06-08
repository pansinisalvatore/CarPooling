package uk.co.maxcarli.carpooling;

    public class Utenti{

        public String nome;
        public String cognome;
        public String codice_fiscale;

        public Utenti() {
        }

        public Utenti(String nome, String cognome, String codice_fiscale) {
            this.nome = nome;
            this.cognome = cognome;
            this.codice_fiscale = codice_fiscale;
        }

        public String getNome() {
            return nome;
        }

        public String getCognome() {
            return cognome;
        }

        public String getCodice_fiscale() {
            return codice_fiscale;
        }
    }
