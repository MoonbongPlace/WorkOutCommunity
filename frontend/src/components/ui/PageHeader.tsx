interface Props {
  title: string
  description?: string
}

export default function PageHeader({ title, description }: Props) {
  return (
    <div className="mb-6 border-b border-[#E8E7D1] pb-4">
      <h1 className="text-2xl font-bold text-[#7A7F3A]">{title}</h1>
      {description && (
        <p className="mt-1 text-sm text-gray-500">{description}</p>
      )}
    </div>
  )
}
