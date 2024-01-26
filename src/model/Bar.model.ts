export class IBar {
  id?: number;
  name?: string | null;
  angebotsnummer?: number | null;
  schlagwort?: string | null;
  versicherungsbeginn?: string | Date | null;
  buJN?: boolean | null;
}

export class Bar implements IBar {
  constructor(
    public id?: number,
    public name?: string | null,
    public angebotsnummer?: number | null,
    public schlagwort?: string | null,
    public versicherungsbeginn?: string | null,
    public buJN?: boolean | null
  ) {}
}
