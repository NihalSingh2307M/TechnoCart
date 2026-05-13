import { useState, useMemo } from "react";

import { employeeApi }   from "./api/employeeApi";
import { useEmployees }  from "./hooks/useEmployees";
import { useToast }      from "./hooks/useToast";

import Header          from "./components/Header";
import Toolbar         from "./components/Toolbar";
import EmployeeTable   from "./components/EmployeeTable";
import EmployeeModal   from "./components/EmployeeModal";
import ConfirmDialog   from "./components/ConfirmDialog";
import Toast           from "./components/Toast";

export default function App() {
  // ── Filter / sort state ──────────────────────────────────
  const [search,     setSearch]     = useState("");
  const [deptFilter, setDeptFilter] = useState("");
  const [sortBy,     setSortBy]     = useState("");

  // ── Modal state ──────────────────────────────────────────
  const [modal,        setModal]        = useState(null);  // "add" | "edit" | null
  const [editTarget,   setEditTarget]   = useState(null);
  const [deleteTarget, setDeleteTarget] = useState(null);

  // ── Async loading flags ──────────────────────────────────
  const [saving,   setSaving]   = useState(false);
  const [deleting, setDeleting] = useState(false);

  // ── Data & notifications ─────────────────────────────────
  const { employees, loading, connected, refetch } = useEmployees(sortBy);
  const { toasts, toast } = useToast();

  // ── Derived data ─────────────────────────────────────────
  const depts = useMemo(
    () => [...new Set(employees.map((e) => e.department).filter(Boolean))].sort(),
    [employees]
  );

  const filtered = useMemo(() => {
    const q = search.toLowerCase();
    return employees.filter((e) => {
      const fullName = `${e.firstName} ${e.lastName}`.toLowerCase();
      const matchSearch =
        !q || fullName.includes(q) || e.email.toLowerCase().includes(q);
      const matchDept =
        !deptFilter || e.department === deptFilter;
      return matchSearch && matchDept;
    });
  }, [employees, search, deptFilter]);

  // ── Modal handlers ───────────────────────────────────────
  const openAdd  = () => { setEditTarget(null); setModal("add"); };
  const openEdit = (emp) => { setEditTarget(emp); setModal("edit"); };
  const closeModal = () => { setModal(null); setEditTarget(null); };

  // ── CRUD handlers ────────────────────────────────────────
  const handleSave = async (formData) => {
    setSaving(true);
    try {
      if (modal === "add") {
        await employeeApi.create(formData);
        toast.success("Employee added successfully!");
      } else {
        await employeeApi.update(editTarget.id, formData);
        toast.success("Employee updated successfully!");
      }
      closeModal();
      refetch();
    } catch (err) {
      toast.error(`Save failed: ${err.message}`);
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    setDeleting(true);
    try {
      await employeeApi.delete(deleteTarget.id);
      toast.success(`${deleteTarget.firstName} ${deleteTarget.lastName} removed.`);
      setDeleteTarget(null);
      refetch();
    } catch (err) {
      toast.error(`Delete failed: ${err.message}`);
    } finally {
      setDeleting(false);
    }
  };

  // ── Render ───────────────────────────────────────────────
  return (
    <div className="min-h-screen bg-zinc-950 text-zinc-100">
      <div className="max-w-6xl mx-auto px-6 py-10">

        <Header
          total={employees.length}
          departments={depts.length}
          connected={connected}
        />

        <Toolbar
          search={search}       onSearch={setSearch}
          sortBy={sortBy}       onSort={setSortBy}
          depts={depts}
          deptFilter={deptFilter} onDeptFilter={setDeptFilter}
          onAdd={openAdd}
          onRefresh={refetch}
        />

        <EmployeeTable
          employees={filtered}
          loading={loading}
          search={search}
          onEdit={openEdit}
          onDelete={setDeleteTarget}
        />

        <p className="text-xs text-zinc-600 mt-4 text-right">
          Showing {filtered.length} of {employees.length} employees
        </p>
      </div>

      {/* Modals */}
      {modal && (
        <EmployeeModal
          mode={modal}
          employee={editTarget}
          onSave={handleSave}
          onClose={closeModal}
          saving={saving}
        />
      )}

      <ConfirmDialog
        employee={deleteTarget}
        onConfirm={handleDelete}
        onCancel={() => setDeleteTarget(null)}
        loading={deleting}
      />

      {/* Notifications */}
      <Toast toasts={toasts} />
    </div>
  );
}
