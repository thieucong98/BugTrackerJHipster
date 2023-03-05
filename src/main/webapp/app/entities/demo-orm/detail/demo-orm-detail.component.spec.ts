import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DemoOrmDetailComponent } from './demo-orm-detail.component';

describe('DemoOrm Management Detail Component', () => {
  let comp: DemoOrmDetailComponent;
  let fixture: ComponentFixture<DemoOrmDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DemoOrmDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ demoOrm: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DemoOrmDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DemoOrmDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load demoOrm on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.demoOrm).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
