export default function Select({
  label,
  error,
  children,
  className = "",
  ...props
}) {
  return (
    <div className="flex flex-col gap-1.5">
      {label && (
        <label className="text-xs font-semibold uppercase tracking-widest text-zinc-500">
          {label}
        </label>
      )}
      <select
        className={`
          w-full bg-zinc-900 border rounded-xl px-3.5 py-2.5
          text-sm text-zinc-100 outline-none cursor-pointer
          transition-all duration-150
          ${error
            ? "border-rose-500 focus:border-rose-400"
            : "border-zinc-700 focus:border-lime-400"
          }
          ${className}
        `}
        {...props}
      >
        {children}
      </select>
      {error && (
        <p className="text-xs text-rose-400">{error}</p>
      )}
    </div>
  );
}
