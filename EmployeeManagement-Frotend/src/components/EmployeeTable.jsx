import EmployeeRow from "./EmployeeRow";

const COLUMNS = ["ID", "Employee", "Email", "Department", "Actions"];

function SkeletonRows() {
  return Array.from({ length: 5 }).map((_, i) => (
    <tr key={i} className="border-b border-zinc-800/60">
      {[30, 50, 60, 35, 40].map((w, j) => (
        <td key={j} className="px-5 py-4">
          <div
            className="h-4 bg-zinc-800 rounded-full animate-pulse"
            style={{ width: `${w + (i * 7) % 20}%` }}
          />
        </td>
      ))}
    </tr>
  ));
}

function EmptyState({ search }) {
  return (
    <tr>
      <td colSpan={5} className="px-5 py-24 text-center">
        <div className="flex flex-col items-center gap-3">
          <div className="w-16 h-16 rounded-2xl bg-zinc-800 flex items-center justify-center text-3xl">
            {search ? "🔍" : "👤"}
          </div>
          <p className="text-zinc-300 font-semibold text-base">
            {search ? "No employees match your search" : "No employees yet"}
          </p>
          <p className="text-zinc-600 text-sm">
            {search
              ? "Try different keywords or clear filters"
              : "Click '+ Add Employee' to get started"}
          </p>
        </div>
      </td>
    </tr>
  );
}

export default function EmployeeTable({
  employees,
  loading,
  search,
  onEdit,
  onDelete,
}) {
  return (
    <div className="rounded-2xl border border-zinc-800 overflow-hidden bg-zinc-950">
      <div className="overflow-x-auto">
        <table className="w-full">
          <thead>
            <tr className="border-b border-zinc-800 bg-zinc-900/80">
              {COLUMNS.map((col) => (
                <th
                  key={col}
                  className="px-5 py-3.5 text-left text-xs font-bold uppercase tracking-widest text-zinc-500"
                >
                  {col}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <SkeletonRows />
            ) : employees.length === 0 ? (
              <EmptyState search={search} />
            ) : (
              employees.map((emp) => (
                <EmployeeRow
                  key={emp.id}
                  employee={emp}
                  onEdit={onEdit}
                  onDelete={onDelete}
                />
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
