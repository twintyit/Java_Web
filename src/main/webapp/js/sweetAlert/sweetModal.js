function confirmAction({ title, text, confirmButtonText, cancelButtonText, onConfirm, onCancel }) {
    const swalWithBootstrapButtons = Swal.mixin({
        customClass: {
            confirmButton: "btn sweet-btn btn-success",
            cancelButton: "btn sweet-btn btn-danger"
        },
        buttonsStyling: false
    });

    swalWithBootstrapButtons.fire({
        title: title || "Are you sure?",
        text: text || "You won't be able to revert this!",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: confirmButtonText || "Yes, delete it!",
        cancelButtonText: cancelButtonText || "No, cancel!",
        reverseButtons: true
    }).then((result) => {
        if (result.isConfirmed) {
            // Если пользователь подтвердил действие
            if (onConfirm) {
                onConfirm();
            }
        } else if (result.dismiss === Swal.DismissReason.cancel) {
            // Если пользователь отменил действие
            if (onCancel) {
                onCancel();
            }
        }
    });
}
