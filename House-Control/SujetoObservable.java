interface SujetoObservable {
    void registrarObservador(Observador observador);
    void notificarObservadores(String accion);
}
