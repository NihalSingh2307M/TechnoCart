import DeptBadge from "./DeptBadge";
import Button from "./Button";

const AVATAR_COLORS = [
  "bg-violet-500",
  "bg-pink-500",
  "bg-sky-500",
  "bg-emerald-500",
  "bg-amber-500",
  "bg-teal-500",
  "bg-rose-500",
];

export default function EmployeeRow({ employee, onEdit, onDelete }) {
  const { id, firstName, lastName, email, department } = employee;

  const initials =
    `${firstName?.[0] || ""}${lastName?.[0] || ""}`.toUpperCase() || "?";

  const avatarColor = AVATAR_COLORS[id % AVATAR_COLORS.length];

  return (
    <tr className="border-b border-zinc-800/60 hover:bg-zinc-800/30 transition-colors duration-150 group">

      {/* ID */}
      <td className="px-5 py-4">
        <span className="text-xs text-zinc-600 font-mono">#{id}</span>
      </td>

      {/* Name + Avatar */}
      <td className="px-5 py-4">
        <div className="flex items-center gap-3">
          <div
            className={`${avatarColor} w-8 h-8 rounded-full flex items-center justify-center text-white text-xs font-bold flex-shrink-0`}
          >
            {initials}
          </div>
          <span className="text-sm font-semibold text-zinc-100">
            {firstName} {lastName}
          </span>
        </div>
      </td>

      {/* Email */}
      <td className="px-5 py-4">
        <span className="text-sm text-sky-400 font-mono">{email}</span>
      </td>

      {/* Department */}
      <td className="px-5 py-4">
        <DeptBadge department={department} />
      </td>

      {/* Actions — revealed on row hover */}
      <td className="px-5 py-4">
        <div className="flex items-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity duration-150">
          <Button variant="ghost" size="sm" onClick={() => onEdit(employee)}>
            Edit
          </Button>
          <Button variant="danger" size="sm" onClick={() => onDelete(employee)}>
            Delete
          </Button>
        </div>
      </td>
    </tr>
  );
}
