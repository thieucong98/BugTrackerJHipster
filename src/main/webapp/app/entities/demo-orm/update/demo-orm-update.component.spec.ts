import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DemoOrmFormService } from './demo-orm-form.service';
import { DemoOrmService } from '../service/demo-orm.service';
import { IDemoOrm } from '../demo-orm.model';

import { DemoOrmUpdateComponent } from './demo-orm-update.component';

describe('DemoOrm Management Update Component', () => {
  let comp: DemoOrmUpdateComponent;
  let fixture: ComponentFixture<DemoOrmUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let demoOrmFormService: DemoOrmFormService;
  let demoOrmService: DemoOrmService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DemoOrmUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DemoOrmUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemoOrmUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demoOrmFormService = TestBed.inject(DemoOrmFormService);
    demoOrmService = TestBed.inject(DemoOrmService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const demoOrm: IDemoOrm = { id: 456 };

      activatedRoute.data = of({ demoOrm });
      comp.ngOnInit();

      expect(comp.demoOrm).toEqual(demoOrm);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDemoOrm>>();
      const demoOrm = { id: 123 };
      jest.spyOn(demoOrmFormService, 'getDemoOrm').mockReturnValue(demoOrm);
      jest.spyOn(demoOrmService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demoOrm });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demoOrm }));
      saveSubject.complete();

      // THEN
      expect(demoOrmFormService.getDemoOrm).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(demoOrmService.update).toHaveBeenCalledWith(expect.objectContaining(demoOrm));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDemoOrm>>();
      const demoOrm = { id: 123 };
      jest.spyOn(demoOrmFormService, 'getDemoOrm').mockReturnValue({ id: null });
      jest.spyOn(demoOrmService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demoOrm: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demoOrm }));
      saveSubject.complete();

      // THEN
      expect(demoOrmFormService.getDemoOrm).toHaveBeenCalled();
      expect(demoOrmService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDemoOrm>>();
      const demoOrm = { id: 123 };
      jest.spyOn(demoOrmService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demoOrm });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demoOrmService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
