export default function Header({ total, departments, connected }) {
  return (
    <header className="border-b border-zinc-800 pb-6 mb-8">

      {/* Offline banner */}
      {!connected && (
        <div className="mb-5 flex items-center gap-3 bg-rose-500/10 border border-rose-500/30 text-rose-400 text-xs rounded-xl px-4 py-3">
          <span className="w-2 h-2 rounded-full bg-rose-500 animate-pulse flex-shrink-0" />
          <span>
            Backend unreachable — make sure Spring Boot is running on{" "}
            <code className="font-mono bg-rose-500/10 px-1.5 py-0.5 rounded">localhost:8081</code>
          </span>
        </div>
      )}

      <div className="flex items-end justify-between">
        {/* Branding */}
        <div>
          <div className="flex items-center gap-2 mb-1">
            <span className="w-2 h-2 rounded-full bg-lime-400" />
            <span className="text-xs text-zinc-500 uppercase tracking-widest font-semibold">
              Employee Management System
            </span>
          </div>
          <h1 className="text-3xl font-black text-zinc-100 tracking-tight">
            EmpTrack<span className="text-lime-400">.</span>
          </h1>
        </div>

        {/* Stats */}
        <div className="flex gap-8">
          <div className="text-right">
            <div className="text-3xl font-black text-lime-400 leading-none tabular-nums">
              {total}
            </div>
            <div className="text-xs text-zinc-500 uppercase tracking-widest mt-1">
              Employees
            </div>
          </div>
          <div className="text-right">
            <div className="text-3xl font-black text-sky-400 leading-none tabular-nums">
              {departments}
            </div>
            <div className="text-xs text-zinc-500 uppercase tracking-widest mt-1">
              Departments
            </div>
          </div>
        </div>
      </div>
    </header>
  );
}
