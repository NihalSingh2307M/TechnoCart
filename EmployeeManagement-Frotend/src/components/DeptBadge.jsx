const DEPT_COLORS = {
  Engineering: "bg-violet-500/10 text-violet-400 ring-violet-500/20",
  Marketing:   "bg-pink-500/10   text-pink-400   ring-pink-500/20",
  HR:          "bg-amber-500/10  text-amber-400  ring-amber-500/20",
  Finance:     "bg-emerald-500/10 text-emerald-400 ring-emerald-500/20",
  Design:      "bg-sky-500/10    text-sky-400    ring-sky-500/20",
  Operations:  "bg-orange-500/10 text-orange-400 ring-orange-500/20",
  Sales:       "bg-teal-500/10   text-teal-400   ring-teal-500/20",
};

const DEFAULT_COLOR = "bg-zinc-500/10 text-zinc-400 ring-zinc-500/20";

export default function DeptBadge({ department }) {
  if (!department) {
    return <span className="text-zinc-600 text-sm">—</span>;
  }

  const colorClass = DEPT_COLORS[department] || DEFAULT_COLOR;

  return (
    <span
      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-semibold ring-1 ${colorClass}`}
    >
      {department}
    </span>
  );
}
