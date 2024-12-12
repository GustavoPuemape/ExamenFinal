const url = "/v1/eventos";

// Función para realizar la solicitud AJAX
function ajaxRequest(type, endpoint, data = null) {
    return $.ajax({
        type,
        url: endpoint,
        data: data ? JSON.stringify(data) : null,
        dataType: "json",
        contentType: data ? "application/json" : undefined,
        cache: false,
        timeout: 600000,
    });
}

// Función para guardar un nuevo evento o actualizar uno existente
function save(bandera) {
    const id = $("#guardar").data("id");
    const evento = {
        id,
        nombre: $("#nombre").val(),
        descripcion: $("#descripcion").val(),
        fechaInicio: $("#fechaInicio").val(),
        fechaFin: $("#fechaFin").val(),
        costo: $("#costo").val()
    };

    const type = bandera === 1 ? "POST" : "PUT";
    const endpoint = bandera === 1 ? url : `${url}/${id}`;

    ajaxRequest(type, endpoint, evento)
        .done((data) => {
            if (data.ok) {
                $("#modal-update").modal("hide");
                getTabla();
                $("#error-message").addClass("d-none");
                Swal.fire({
                    icon: 'success',
                    title: `Se ha ${bandera === 1 ? 'guardado' : 'actualizado'} el evento`,
                    showConfirmButton: false,
                    timer: 1500
                });
                clear();
            } else {
                showError(data.message);
            }
        }).fail(function (jqXHR) {
            let errorMessage = jqXHR.responseJSON && jqXHR.responseJSON.message ? jqXHR.responseJSON.message : "Error inesperado. Código: " + jqXHR.status;
            showError(errorMessage);
        });
}

// Mostrar el mensaje de error
function showError(message) {
    $("#error-message").text(message).removeClass("d-none");
}

// Eliminar evento
function deleteFila(id) {
    ajaxRequest("DELETE", `${url}/${id}`)
        .done((data) => {
            if (data.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Se ha eliminado el evento',
                    showConfirmButton: false,
                    timer: 1500
                });
                getTabla();
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.message,
                    confirmButtonText: 'Aceptar'
                });
            }
        })
        .fail(handleError);
}

// Obtener los datos de la tabla
function getTabla() {
    ajaxRequest("GET", url)
        .done((data) => {
            const t = $("#tablaRegistros").DataTable();
            t.clear().draw(false);

            if (data.ok) {
                $.each(data.body, (index, evento) => {
                    const botonera = `
                        <button type="button" class="btn btn-warning btn-xs editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-danger btn-xs eliminar">
                            <i class="fas fa-trash"></i>
                        </button>`;

                    t.row.add([botonera, evento.id, evento.nombre, evento.descripcion, evento.fechaInicio, evento.fechaFin, evento.costo]);
                });
                t.draw();
            } else {
                console.error("Error en la respuesta: ", data.message);
            }
        })
        .fail(handleError);
}

// Obtener los datos de un evento específico
function getFila(id) {
    ajaxRequest("GET", `${url}/${id}`)
        .done((data) => {
            if (data.ok) {
                $("#modal-title").text("Editar evento");
                $("#nombre").val(data.body.nombre);
                $("#descripcion").val(data.body.descripcion);
                $("#fechaInicio").val(data.body.fechaInicio);
                $("#fechaFin").val(data.body.fechaFin);
                $("#costo").val(data.body.costo);
                $("#guardar").data("id", data.body.id).data("bandera", 0);
                $("#modal-update").modal("show");
            } else {
                showError(data.message);
            }
        })
        .fail(handleError);
}

// Limpiar los campos del formulario
function clear() {
    $("#modal-title").text("Nuevo Evento");
    $("#nombre").val("");
    $("#descripcion").val("");
    $("#fechaInicio").val("");
    $("#fechaFin").val("");
    $("#costo").val("");
    $("#guardar").data("id", 0).data("bandera", 1);
}

// Manejar errores generales
function handleError(jqXHR) {
    const errorMessage = jqXHR.responseJSON?.message || `Error inesperado. Código: ${jqXHR.status}`;
    Swal.fire({
        icon: 'error',
        title: 'Error',
        text: errorMessage,
        confirmButtonText: 'Aceptar'
    });
}

// Inicializar tabla y eventos
$(document).ready(function () {
    // Inicializar la tabla DataTable
    $("#tablaRegistros").DataTable({
        language: {
            lengthMenu: "Mostrar _MENU_ eventos",
            zeroRecords: "No se encontraron coincidencias",
            info: "Mostrando del _START_ al _END_ de _TOTAL_ eventos",
            infoEmpty: "Sin resultados",
            search: "Buscar: ",
            paginate: {
                first: "Primero",
                last: "Último",
                next: "Siguiente",
                previous: "Anterior",
            },
        },
        columnDefs: [
            { targets: 0, orderable: false }
        ],
    });

    // Limpiar formulario al cargar la página
    clear();

    // Abrir el formulario para crear un nuevo evento
    $("#nuevo").click(clear);

    // Guardar evento al hacer clic en el botón de guardar
    $("#guardar").click(() => save($("#guardar").data("bandera")));

    // Eliminar evento al hacer clic en el botón de eliminar
    $(document).on('click', '.eliminar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        Swal.fire({
            title: 'Eliminar evento',
            text: "¿Está seguro de querer eliminar este evento?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Si'
        }).then((result) => {
            if (result.isConfirmed) {
                deleteFila(id);
            }
        });
    });

    // Editar evento al hacer clic en el botón de editar
    $(document).on('click', '.editar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        getFila(id);
    });

    // Cargar la tabla con los eventos
    getTabla();
});
