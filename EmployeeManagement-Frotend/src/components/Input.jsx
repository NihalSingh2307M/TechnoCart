export default function Input({
  label,
  error,
  type = "text",
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
      <input
        type={type}
        className={`
          w-full bg-zinc-900 border rounded-xl px-3.5 py-2.5
          text-sm text-zinc-100 placeholder:text-zinc-600
          outline-none transition-all duration-150
          ${error
            ? "border-rose-500 focus:border-rose-400"
            : "border-zinc-700 focus:border-lime-400"
          }
          ${className}
        `}
        {...props}
      />
      {error && (
        <p className="text-xs text-rose-400">{error}</p>
      )}
    </div>
  );
}
