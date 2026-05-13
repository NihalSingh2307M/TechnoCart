import Button from "./Button";

export default function ConfirmDialog({
  employee,    // employee to delete (null = hidden)
  onConfirm,
  onCancel,
  loading,
}) {
  if (!employee) return null;

  return (
    <div
      className="fixed inset-0 z-40 flex items-center justify-center bg-black/70 backdrop-blur-sm"
      onClick={(e) => e.target === e.currentTarget && onCancel()}
    >
      <div className="bg-zinc-900 border border-zinc-800 rounded-2xl w-full max-w-sm mx-4 shadow-2xl p-6">

        {/* Icon + title */}
        <div className="flex items-center gap-4 mb-5">
          <div className="w-12 h-12 rounded-xl bg-rose-500/10 border border-rose-500/20 flex items-center justify-center text-rose-400 text-2xl flex-shrink-0">
            ⚠
          </div>
          <div>
            <h3 className="font-bold text-zinc-100 text-base">Delete Employee</h3>
            <p className="text-xs text-zinc-500 mt-0.5">This action cannot be undone</p>
          </div>
        </div>

        {/* Body */}
        <p className="text-sm text-zinc-400 mb-6 leading-relaxed">
          Are you sure you want to permanently remove{" "}
          <span className="text-zinc-100 font-semibold">
            {employee.firstName} {employee.lastName}
          </span>{" "}
          from the system?
        </p>

        {/* Actions */}
        <div className="flex justify-end gap-3">
          <Button variant="ghost" onClick={onCancel} disabled={loading}>
            Cancel
          </Button>
          <Button
            variant="danger"
            onClick={onConfirm}
            loading={loading}
            className="bg-rose-500! text-white! border-rose-500!"
          >
            Yes, Delete
          </Button>
        </div>
      </div>
    </div>
  );
}
