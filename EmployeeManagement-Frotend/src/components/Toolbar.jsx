import Input from "./Input";
import Select from "./Select";
import Button from "./Button";

const SORT_OPTIONS = [
  { value: "",           label: "Sort: Default"    },
  { value: "firstName",  label: "Sort: First Name" },
  { value: "lastName",   label: "Sort: Last Name"  },
  { value: "department", label: "Sort: Department" },
];

export default function Toolbar({
  search,
  onSearch,
  sortBy,
  onSort,
  depts,
  deptFilter,
  onDeptFilter,
  onAdd,
  onRefresh,
}) {
  return (
    <div className="flex flex-wrap gap-3 items-end mb-6">

      {/* Search */}
      <div className="flex-1 min-w-52">
        <Input
          placeholder="Search by name or email…"
          value={search}
          onChange={(e) => onSearch(e.target.value)}
        />
      </div>

      {/* Department filter */}
      <div className="min-w-44">
        <Select value={deptFilter} onChange={(e) => onDeptFilter(e.target.value)}>
          <option value="">All Departments</option>
          {depts.map((d) => (
            <option key={d} value={d}>{d}</option>
          ))}
        </Select>
      </div>

      {/* Sort */}
      <div className="min-w-44">
        <Select value={sortBy} onChange={(e) => onSort(e.target.value)}>
          {SORT_OPTIONS.map((o) => (
            <option key={o.value} value={o.value}>{o.label}</option>
          ))}
        </Select>
      </div>

      {/* Actions */}
      <Button variant="secondary" onClick={onRefresh} title="Refresh">↻</Button>
      <Button onClick={onAdd}>+ Add Employee</Button>
    </div>
  );
}
