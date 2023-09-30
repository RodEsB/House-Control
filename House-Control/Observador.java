class Observador {
    private SeHaSeleccionado panelAcciones;

    public Observador(SeHaSeleccionado panelAcciones) {
        this.panelAcciones = panelAcciones;
    }

    public void actualizar(String accion) {
        panelAcciones.mostrarAccion("Se ha seleccionado: " + accion);
    }
}
