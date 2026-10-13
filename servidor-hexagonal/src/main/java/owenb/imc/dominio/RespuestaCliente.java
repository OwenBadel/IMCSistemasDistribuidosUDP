package owenb.imc.dominio;

public record RespuestaCliente(TipoRespuesta tipo, ResultadoImc resultado, String error, Destinatario destinatario) {
    public RespuestaCliente {
        if (tipo == null) {
            throw new RespuestaInvalidaException("El tipo de respuesta es obligatorio.");
        }
        if (destinatario == null) {
            throw new RespuestaInvalidaException("El destinatario es obligatorio.");
        }
        if (tipo == TipoRespuesta.EXITO && resultado == null) {
            throw new RespuestaInvalidaException("Una respuesta exitosa requiere un resultado de IMC.");
        }
        if (tipo == TipoRespuesta.ERROR && (error == null || error.isBlank())) {
            throw new RespuestaInvalidaException("Una respuesta de error requiere un mensaje de error.");
        }
    }

    public static RespuestaCliente exitosa(ResultadoImc resultado, Destinatario destinatario) {
        return new RespuestaCliente(TipoRespuesta.EXITO, resultado, null, destinatario);
    }

    public static RespuestaCliente fallida(String error, Destinatario destinatario) {
        return new RespuestaCliente(TipoRespuesta.ERROR, null, error, destinatario);
    }
}
