const VARIANTS = {
  primary:   "bg-lime-400 text-zinc-900 hover:bg-lime-300 font-bold border-transparent",
  ghost:     "bg-transparent text-zinc-300 border border-zinc-700 hover:border-lime-400 hover:text-lime-400",
  danger:    "bg-transparent text-rose-400 border border-rose-500/40 hover:bg-rose-500 hover:text-white",
  secondary: "bg-zinc-800 text-zinc-300 border-transparent hover:bg-zinc-700",
};

const SIZES = {
  sm: "px-3 py-1.5 text-xs rounded-lg",
  md: "px-4 py-2   text-sm rounded-xl",
  lg: "px-6 py-2.5 text-sm rounded-xl",
};

export default function Button({
  children,
  variant = "primary",
  size = "md",
  disabled = false,
  loading = false,
  onClick,
  type = "button",
  className = "",
}) {
  return (
    <button
      type={type}
      disabled={disabled || loading}
      onClick={onClick}
      className={`
        inline-flex items-center gap-2 font-medium tracking-wide transition-all duration-150
        cursor-pointer disabled:opacity-40 disabled:cursor-not-allowed
        ${VARIANTS[variant]} ${SIZES[size]} ${className}
      `}
    >
      {loading && (
        <span className="w-3.5 h-3.5 border-2 border-current border-t-transparent rounded-full animate-spin" />
      )}
      {children}
    </button>
  );
}
