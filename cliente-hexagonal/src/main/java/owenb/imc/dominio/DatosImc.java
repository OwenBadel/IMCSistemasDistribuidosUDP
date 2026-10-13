package owenb.imc.dominio;

public record DatosImc(Peso peso, Altura altura, DestinoServidor destino) {
    public DatosImc {
        if (peso == null || altura == null || destino == null) {
            throw new DominioException("Todos los datos de peso, altura y destino son obligatorios.") {};
        }
    }
}
