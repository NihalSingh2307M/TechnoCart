const TYPE_CONFIG = {
  success: { icon: "✓", bar: "bg-emerald-500", dot: "bg-emerald-500" },
  error:   { icon: "✕", bar: "bg-rose-500",    dot: "bg-rose-500"    },
  info:    { icon: "i", bar: "bg-sky-500",      dot: "bg-sky-500"     },
};

function ToastItem({ message, type }) {
  const cfg = TYPE_CONFIG[type] || TYPE_CONFIG.info;
  return (
    <div className="flex items-center gap-3 bg-zinc-900 border border-zinc-700 rounded-xl px-4 py-3 shadow-2xl min-w-64">
      <span
        className={`${cfg.bar} text-white rounded-full w-5 h-5 flex items-center justify-center text-xs font-bold flex-shrink-0`}
      >
        {cfg.icon}
      </span>
      <span className="text-sm text-zinc-200 font-medium">{message}</span>
    </div>
  );
}

export default function Toast({ toasts }) {
  if (toasts.length === 0) return null;

  return (
    <div className="fixed bottom-6 right-6 flex flex-col gap-3 z-50">
      {toasts.map((t) => (
        <ToastItem key={t.id} message={t.message} type={t.type} />
      ))}
    </div>
  );
}
